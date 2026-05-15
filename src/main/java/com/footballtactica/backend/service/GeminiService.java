package com.footballtactica.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

    public String analyze(String prompt) {
        try {
            String url = GEMINI_URL + apiKey;
            Map<String, Object> part = Map.of("text", prompt);
            Map<String, Object> content = Map.of("parts", List.of(part));
            Map<String, Object> body = Map.of("contents", List.of(content));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List candidates = (List) response.getBody().get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map contentResponse = (Map) candidate.get("content");
                    List parts = (List) contentResponse.get("parts");
                    Map firstPart = (Map) parts.get(0);
                    return (String) firstPart.get("text");
                }
            }
            return "No se pudo generar el análisis.";
        } catch (Exception e) {
            return "Error al conectar con Gemini: " + e.getMessage();
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
    try {
        String url = GEMINI_URL + apiKey;

        String base64Video = java.util.Base64.getEncoder().encodeToString(videoBytes);

        Map<String, Object> textPart = Map.of("text",
            "Eres un analista táctico profesional de fútbol con 20 años de experiencia. " +
            "Analiza este video de fútbol con el mayor detalle posible. " +
            "Instrucción específica del analista: " + userPrompt + "\n\n" +
            "En tu análisis incluye:\n" +
            "## DESCRIPCIÓN GENERAL DE LA JUGADA\n" +
            "## ANÁLISIS INDIVIDUAL DE JUGADORES\n" +
            "Identifica cada jugador por su dorsal y equipo. Analiza sus movimientos, decisiones y rendimiento.\n" +
            "## ANÁLISIS COLECTIVO\n" +
            "## ERRORES DETECTADOS\n" +
            "## ACIERTOS DETECTADOS\n" +
            "## RECOMENDACIONES TÁCTICAS\n" +
            "## CALIFICACIÓN GENERAL (1-10)\n\n" +
            "Sé extremadamente detallado y profesional."
        );

        Map<String, Object> inlineData = Map.of(
            "mime_type", mimeType,
            "data", base64Video
        );
        Map<String, Object> videoPart = Map.of("inline_data", inlineData);

        Map<String, Object> content = Map.of("parts", List.of(textPart, videoPart));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List candidates = (List) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map candidate = (Map) candidates.get(0);
                Map contentResponse = (Map) candidate.get("content");
                List parts = (List) contentResponse.get("parts");
                Map firstPart = (Map) parts.get(0);
                return (String) firstPart.get("text");
            }
        }
        return "No se pudo analizar el video.";
    } catch (Exception e) {
        return "Error al analizar el video: " + e.getMessage();
    }
}

public String analyzeVideoUrl(String videoUrl, String userPrompt) {
    try {
        String url = GEMINI_URL + apiKey;

        Map<String, Object> textPart = Map.of("text",
            "Eres un analista táctico profesional de fútbol con 20 años de experiencia. " +
            "Analiza este video de fútbol con el mayor detalle posible. " +
            "Instrucción específica del analista: " + userPrompt + "\n\n" +
            "En tu análisis incluye:\n" +
            "## DESCRIPCIÓN GENERAL DE LA JUGADA\n" +
            "## ANÁLISIS INDIVIDUAL DE JUGADORES\n" +
            "## ANÁLISIS COLECTIVO\n" +
            "## ERRORES DETECTADOS\n" +
            "## ACIERTOS DETECTADOS\n" +
            "## RECOMENDACIONES TÁCTICAS\n" +
            "## CALIFICACIÓN GENERAL (1-10)\n"
        );

        Map<String, Object> fileData = Map.of(
            "mime_type", "video/mp4",
            "file_uri", videoUrl
        );
        Map<String, Object> videoPart = Map.of("file_data", fileData);

        Map<String, Object> content = Map.of("parts", List.of(textPart, videoPart));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List candidates = (List) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map candidate = (Map) candidates.get(0);
                Map contentResponse = (Map) candidate.get("content");
                List parts = (List) contentResponse.get("parts");
                Map firstPart = (Map) parts.get(0);
                return (String) firstPart.get("text");
            }
        }
        return "No se pudo analizar el video.";
    }   catch (Exception e) {
        return "Error: " + e.getMessage();
        }
    }
}