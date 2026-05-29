package com.footballtactica.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class GeminiService {

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String CLAUDE_URL = "https://api.anthropic.com/v1/messages";
    // Usamos el modelo público estrella que acepta tu cuenta y que da los mejores reportes tácticos
    private static final String CLAUDE_MODEL = "claude-haiku-4-5-20251001";
    public String analyze(String prompt) {
        try {
            // 1. Configurar los Headers oficiales exigidos por Anthropic
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", "2023-06-01"); // Obligatorio para la API de mensajes

            // 2. Construir la estructura exacta del mensaje del usuario
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", prompt);

            // 3. Armar el Body de la petición
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", CLAUDE_MODEL);
            body.put("max_tokens", 2048);
            body.put("messages", List.of(message));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            
            // Recibimos la respuesta como String crudo para evitar problemas de mapeo con RestTemplate
            ResponseEntity<String> response = restTemplate.postForEntity(CLAUDE_URL, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Usamos ObjectMapper de Jackson para leer el árbol JSON de manera segura
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode contentArray = root.path("content");
                
                if (contentArray.isArray() && !contentArray.isEmpty()) {
                    JsonNode firstBlock = contentArray.get(0);
                    JsonNode textNode = firstBlock.path("text");
                    if (!textNode.isMissingNode()) {
                        return textNode.asText();
                    }
                }
            }
            return "No se pudo generar el análisis táctico.";
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // Si la API devuelve un error de cliente (4xx), capturamos el mensaje real del servidor
            return "Error de la API de Anthropic: " + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "Error al conectar con la IA: " + e.getMessage();
        }
    }

    public String analyzePlayer(String playerName, String position, String description) {
        String prompt = """
                Eres un scout y analista táctico profesional de fútbol con 20 años de experiencia trabajando en clubes de élite.
                
                Analiza el siguiente jugador y genera un reporte táctico DETALLADO Y PROFESIONAL en español:
                
                JUGADOR: %s
                POSICIÓN: %s
                DESCRIPCIÓN/OBSERVACIONES: %s
                
                Tu reporte debe incluir OBLIGATORIAMENTE estas secciones con formato claro:
                
                ## PERFIL DEL JUGADOR
                Resumen ejecutivo del jugador.
                
                ## FORTALEZAS TÉCNICAS
                Lista detallada de sus puntos fuertes con ejemplos específicos.
                
                ## ÁREAS DE MEJORA
                Aspectos tácticos y técnicos que debe trabajar.
                
                ## ANÁLISIS TÁCTICO
                Cómo encaja en diferentes sistemas de juego, movimientos sin balón, participación en la presión.
                
                ## ESTADÍSTICAS PROYECTADAS
                Estimación de métricas: pases completados, duelos ganados, km recorridos, etc.
                
                ## RECOMENDACIONES
                Plan de desarrollo específico para los próximos 3 meses.
                
                ## PUNTUACIÓN GENERAL
                Calificación del 1-10 en: Técnica, Táctica, Físico, Mental, Potencial.
                
                Sé específico, profesional y detallado. Este reporte será usado por el cuerpo técnico.
                """.formatted(playerName, position, description);
        return analyze(prompt);
    }

    public String analyzeTactic(String tacticName, String formation, String description) {
        String prompt = """
                Eres un analista táctico de fútbol de élite, especialista en análisis de sistemas de juego.
                
                Analiza la siguiente táctica y genera un reporte DETALLADO Y PROFESIONAL en español:
                
                TÁCTICA: %s
                FORMACIÓN: %s
                DESCRIPCIÓN: %s
                
                Tu análisis debe incluir OBLIGATORIAMENTE:
                
                ## ANÁLISIS DE LA FORMACIÓN
                Descripción detallada de cómo funciona este sistema.
                
                ## FASE OFENSIVA
                Patrones de ataque, movimientos de jugadores, creación de espacios.
                
                ## FASE DEFENSIVA
                Organización defensiva, presión, líneas defensivas.
                
                ## TRANSICIONES
                Cómo el equipo transiciona entre ataque y defensa.
                
                ## FORTALEZAS DEL SISTEMA
                Por qué este sistema puede ser efectivo.
                
                ## VULNERABILIDADES
                Cómo los rivales pueden explotar debilidades.
                
                ## EQUIPOS QUE USAN ESTE SISTEMA
                Referencias de equipos profesionales que utilizan tácticas similares.
                
                ## RECOMENDACIONES
                Ajustes específicos para maximizar la efectividad.
                
                Sé específico y usa terminología táctica profesional.
                """.formatted(tacticName, formation, description);
        return analyze(prompt);
    }

    public String analyzeVideo(String videoDescription, String focusPlayer, String matchContext) {
        String prompt = """
                Eres un analista de vídeo profesional de fútbol, especializado en análisis de partidos y jugadas.
                
                Analiza la siguiente jugada/secuencia de juego descrita y genera un reporte DETALLADO en español:
                
                DESCRIPCIÓN DEL VÍDEO/JUGADA: %s
                JUGADOR EN FOCO: %s
                CONTEXTO DEL PARTIDO: %s
                
                Tu análisis debe incluir:
                
                ## DESCRIPCIÓN DE LA JUGADA
                Narrativa detallada de lo que ocurre en la jugada.
                
                ## ANÁLISIS DEL JUGADOR FOCO
                Movimientos específicos, decisiones tomadas, posicionamiento.
                
                ## ANÁLISIS COLECTIVO
                Cómo interactúa el equipo en esta jugada.
                
                ## PUNTOS CLAVE
                Los 3-5 momentos más importantes de la jugada.
                
                ## ERRORES DETECTADOS
                Errores individuales o colectivos observados.
                
                ## ACIERTOS DETECTADOS
                Aspectos positivos de la jugada.
                
                ## RECOMENDACIONES PARA ENTRENAR
                Ejercicios específicos para mejorar los aspectos identificados.
                
                ## CALIFICACIÓN DE LA JUGADA
                Puntuación del 1-10 con justificación.
                
                Sé extremadamente detallado y profesional.
                """.formatted(videoDescription, focusPlayer, matchContext);
        return analyze(prompt);
    }

    public String comparePlayersAnalysis(String player1, String player2, String position) {
        String prompt = """
                Eres un scout profesional de fútbol con experiencia en análisis comparativo de jugadores.
                
                Compara los siguientes dos jugadores para la posición de %s:
                
                JUGADOR 1: %s
                JUGADOR 2: %s
                
                Tu comparativa debe incluir:
                
                ## TABLA COMPARATIVA
                Tabla con atributos: Técnica, Velocidad, Físico, Táctica, Mental, Potencial (calificación 1-10 cada uno).
                
                ## JUGADOR 1 - ANÁLISIS
                Fortalezas y debilidades específicas.
                
                ## JUGADOR 2 - ANÁLISIS
                Fortalezas y debilidades específicas.
                
                ## COMPARATIVA DIRECTA
                En qué aspectos supera cada jugador al otro.
                
                ## RECOMENDACIÓN FINAL
                Cuál elegirías y por qué, según el contexto táctico.
                
                Sé objetivo y profesional.
                """.formatted(position, player1, player2);
        return analyze(prompt);
    }

    public String analyzeVideoFile(byte[] videoBytes, String mimeType, String userPrompt) {
        String prompt = """
                Eres un analista táctico profesional de fútbol con 20 años de experiencia.
                
                El usuario ha enviado un video de fútbol para analizar.
                Instrucción específica del analista: %s
                
                Genera un análisis táctico profesional completo en español:
                
                ## DESCRIPCIÓN GENERAL DE LA JUGADA
                ## ANÁLISIS INDIVIDUAL DE JUGADORES
                ## ANÁLISIS COLECTIVO
                ## ERRORES DETECTADOS
                ## ACIERTOS DETECTADOS
                ## RECOMENDACIONES TÁCTICAS
                ## CALIFICACIÓN GENERAL (1-10)
                """.formatted(userPrompt);
        return analyze(prompt);
    }

    public String analyzeVideoUrl(String videoUrl, String userPrompt) {
        String prompt = """
                Eres un analista táctico profesional de fútbol con 20 años de experiencia.
                
                Analiza el siguiente video de fútbol: %s
                Instrucción específica del analista: %s
                
                En tu análisis incluye:
                
                ## DESCRIPCIÓN GENERAL DE LA JUGADA
                ## ANÁLISIS INDIVIDUAL DE JUGADORES
                ## ANÁLISIS COLECTIVO
                ## ERRORES DETECTADOS
                ## ACIERTOS DETECTADOS
                ## RECOMENDACIONES TÁCTICAS
                ## CALIFICACIÓN GENERAL (1-10)
                """.formatted(videoUrl, userPrompt);
        return analyze(prompt);
    }
}