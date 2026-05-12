package com.footballtactica.backend.constants;

public class ApiConstants {
    
    public static final String API_BASE = "/api/v1";
    public static final String AUTH_BASE = API_BASE + "/auth";
    public static final String PLAYERS_BASE = API_BASE + "/players";
    public static final String TACTICS_BASE = API_BASE + "/tactics";
    public static final String PLAYS_BASE = API_BASE + "/plays";
    public static final String REPORTS_BASE = API_BASE + "/reports";
    
    public static final String ROLE_COACH = "COACH";
    public static final String ROLE_ANALYST = "ANALYST";
    
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_HEADER = "Authorization";
    
    private ApiConstants() {}
}