package app.doctor.dmcx.app.da.project.doctorapp.Model;

public class Medicine {

    private String name;
    private String dose;
    private String quantity;

    public Medicine() {
    }

    public Medicine(String name, String dose, String quantity) {
        this.name = name;
        this.dose = dose;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getDose() {
        return dose;
    }

    public String getQuantity() {
        return quantity;
    }
}
