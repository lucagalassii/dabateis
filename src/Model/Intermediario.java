package Model;
import java.time.LocalDate;

public class Intermediario {
    private int codiceIntermediario;
    private String nome;
    private String cognome;
    private LocalDate nascita;
    private String luogoNascita;

    public Intermediario(){
    }

    public Intermediario(int codiceIntermediario, String nome, 
        String cognome, LocalDate nascita, String luogoNascita){
            this.codiceIntermediario = codiceIntermediario;
            this.nome = nome;
            this.cognome = cognome;
            this.nascita = nascita;
            this.luogoNascita = luogoNascita;
    }

    public int GetCodiceIntermediario(){
        return codiceIntermediario;
    }
    public void SetCodiceIntermediario(int codiceIntermediario){
        this.codiceIntermediario = codiceIntermediario;
    }
    public String GetNome(){
        return nome;
    }
    public void SetNome(String nome){
        this.nome = nome;
    }
    public String GetCognome(){
        return cognome;
    }
    public void SetCognome(String cognome){
        this.cognome = cognome;
    }
    public LocalDate GetNascita(){
        return nascita;
    }
    public void SetNascita(LocalDate nascita){
        this.nascita = nascita;
    }
    public String GetLuogoNascita(){
        return luogoNascita;
    }
    public void SetLuogoNascita(String luogoNascita){
        this.luogoNascita = luogoNascita;
    }
}
