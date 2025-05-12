public class Pregunta {
    int id;
    String enunciado;
    String opcion1;
    String opcion2;
    String opcion3;
    String correcta;
    String respuestaSeleccionada; // ← nuevo campo

    public Pregunta(int id, String enunciado, String opcion1, String opcion2, String opcion3, String correcta) {


        this.id = id;
        this.enunciado = enunciado;
        this.opcion1 = opcion1;
        this.opcion2 = opcion2;
        this.opcion3 = opcion3;
        this.correcta = correcta;
        this.respuestaSeleccionada = null;
    }
}
