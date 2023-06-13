package net.gymtij;

import java.time.LocalDateTime;

public class Huella {

    // public Integer id;
    public Integer socioId;
    public Integer dedo;
    public String huella;
    // public LocalDateTime fechaEliminado;
    // public LocalDateTime fechaCreado;
    // public LocalDateTime fechaActualizado;

    public Huella() {
        super();
    }
    
    public Integer getSocioId() {
        return socioId;
    }
    public void setSocioId(Integer socioId) {
        this.socioId = socioId;
    }
    public Integer getDedo() {
        return dedo;
    }
    public void setDedo(Integer dedo) {
        this.dedo = dedo;
    }
    public String getHuella() {
        return huella;
    }
    public void setHuella(String huella) {
        this.huella = huella;
    }
}