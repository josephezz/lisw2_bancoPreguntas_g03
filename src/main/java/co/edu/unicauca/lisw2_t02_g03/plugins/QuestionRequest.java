package co.edu.unicauca.lisw2_t02_g03.plugins;

import java.util.List;

/**
 * DTO (Data Transfer Object) que encapsula los datos necesarios para que un plugin
 * genere una pregunta. Actúa como contrato de entrada para {@link QuestionPlugin#generate}.
 */
public class QuestionRequest {

    private String titulo;
    private String contenido;
    private List<String> opciones;
    private String respuestaCorrecta;
    private String competencia;
    private String categoria;
    private String nivelDificultad;
    private String recursoMultimedia;
    private String autorLogin;

    public QuestionRequest() {
    }

    // ============================
    // Getters y Setters
    // ============================

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getCompetencia() {
        return competencia;
    }

    public void setCompetencia(String competencia) {
        this.competencia = competencia;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public String getRecursoMultimedia() {
        return recursoMultimedia;
    }

    public void setRecursoMultimedia(String recursoMultimedia) {
        this.recursoMultimedia = recursoMultimedia;
    }

    public String getAutorLogin() {
        return autorLogin;
    }

    public void setAutorLogin(String autorLogin) {
        this.autorLogin = autorLogin;
    }
}
