public class PreguntaPractica {
    public int id;
    public String enunciado;
    public String opcion1;
    public String opcion2;
    public String opcion3;
    public String correcta; // 1, 2 o 3

    public PreguntaPractica(int id, String enunciado, String o1, String o2, String o3, String correcta) {
        this.id = id;
        this.enunciado = enunciado;
        this.opcion1 = o1;
        this.opcion2 = o2;
        this.opcion3 = o3;
        this.correcta = correcta;
    }
}

