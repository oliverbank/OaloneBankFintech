package oliver.oalone.oalonedevelopers.oalonebank_fintech.Locations;

public class PeruLocationsModel {

    String nombre_ubigeo,id_ubigeo;

    public PeruLocationsModel() {
    }

    public PeruLocationsModel(String nombre_ubigeo, String id_ubigeo) {
        this.nombre_ubigeo = nombre_ubigeo;
        this.id_ubigeo = id_ubigeo;
    }

    public String getNombre_ubigeo() {
        return nombre_ubigeo;
    }

    public void setNombre_ubigeo(String nombre_ubigeo) {
        this.nombre_ubigeo = nombre_ubigeo;
    }

    public String getId_ubigeo() {
        return id_ubigeo;
    }

    public void setId_ubigeo(String id_ubigeo) {
        this.id_ubigeo = id_ubigeo;
    }
}
