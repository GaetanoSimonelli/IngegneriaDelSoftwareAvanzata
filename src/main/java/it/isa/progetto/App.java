package it.isa.progetto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        System.out.println("hello guyz");
        
        Scanner myObj = new Scanner(System.in);
        LocalDate dateObj = LocalDate.now();
        System.out.println("Start "+ dateObj);
        int looper = 1;
        
        //String test = myObj.nextLine();
        //System.out.println("hai scritto"+test);
        

        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Please include Classpath  Where your DB2 Driver is located");
            e.printStackTrace();
            return;
        }
        System.out.println("DB2 driver is loaded successfully");
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean found = false;
        try {
            conn = DriverManager.getConnection("jdbc:db2://localhost:50000/isa:retrieveMessagesFromServerOnGetMessage=true;","db2inst1","pass");
            pstmt = conn.prepareStatement("Select * from Impiegato");
            printa(pstmt);
            pstmt = conn.prepareStatement("SELECT Afferenza, SoddisfazioneAziendale, SupportoColleghi,ComunicazioneInterna FROM Impiegato RIGHT JOIN Questionario ON Impiegato.IdImpiegato = Questionario.Appartenenza ORDER BY Impiegato.Afferenza");
            String s = pDip3(pstmt);
            System.out.print(s);
            pImp2(pstmt);
            pDip1(conn);
            pPro2(conn);
            
        } catch (SQLException e) {
            System.out.println("DB2 Database connection Failed");
            e.printStackTrace();
            return;
        }
    }
    
    public static void printa(PreparedStatement pstmt) {
        try {
            ResultSet rset = pstmt.executeQuery();
            int column_count = (rset.getMetaData()).getColumnCount();
            if (rset != null) {
                while (rset.next()) {
                    for (int i = 1; i <= column_count; i++) {
                        String columnValue = rset.getString(i);
                        System.out.print("   " + rset.getMetaData().getColumnName(i) + " = " + columnValue + "   ");
                    }
                    System.out.print("\n");
                }
            }
        } catch (SQLException ex) {
            System.out.print("Errore nella funzione printa: " + ex);
        }
    }

    public static void create_impiegato(Connection conn, int idImpiegato,int Afferenza, String Nome, String Cognome,LocalDate DataAssunzione ,int Stipendio){
        try{
            String sql = "INSERT INTO Impiegato (IdImpiegato, Afferenza, Nome, Cognome, DataAssunzione, Stipendio) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idImpiegato);
                stmt.setInt(2, Afferenza);
                stmt.setString(3, Nome);
                stmt.setString(4, Cognome);
                stmt.setDate(5, java.sql.Date.valueOf(DataAssunzione));
                stmt.setInt(6, Stipendio);
                int result = stmt.executeUpdate();
                System.out.println("Result: " + result);
            }
        }catch(SQLException ex){
            System.out.print("Errore nella funzione: " + ex);
        }
    }
    
    public static void delete_impiegato(Connection conn, int idImpiegato) {
    try {
        String sql = "DELETE FROM Impiegato WHERE IdImpiegato = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idImpiegato);
            int result = stmt.executeUpdate();
            System.out.println("Result: " + result);
        }
    } catch (SQLException ex) {
        System.out.print("Errore nella funzione: " + ex);
    }
}

    
  

    //query di calcolo
    public static double calcolaStipendio(LocalDate dataAssunzione) {
        // Calcola il numero di mesi tra la data di assunzione e la data corrente
        long mesiPassati = ChronoUnit.MONTHS.between(dataAssunzione, LocalDate.now());

        // Calcola il numero di semestri completi passati
        int semestriPassati = (int) (mesiPassati / 6);
        double importoAggiuntivo = 75 * semestriPassati;
        importoAggiuntivo = Math.min(importoAggiuntivo, 2200);
        double stipendioTotale = 1200 + importoAggiuntivo;

        return stipendioTotale;
    }
    
    public int calcolaCostoPersonale(int numeroDipendenti, int stipendioMedio, int tasseLavoro) {
        if((numeroDipendenti >= 0)&(stipendioMedio >= 0)&(tasseLavoro >= 0)){
            double costoPersonale_double = numeroDipendenti * (stipendioMedio + tasseLavoro);

            if((costoPersonale_double > java.lang.Integer.MAX_VALUE)&&(costoPersonale_double < java.lang.Integer.MIN_VALUE))
            {
                System.out.print("overflow");
                return -1;
            }
            int costoPersonale = numeroDipendenti * (stipendioMedio + tasseLavoro);
            System.out.print("non overflow: "+ costoPersonale_double +"("+costoPersonale+")"+" < " + java.lang.Integer.MAX_VALUE);

            return costoPersonale;
        } 
        else
        {
            System.out.print("negative value");
            return -1;
        }
    }

    public static double calcolaTasse(double redditoAziendale, double aliquoteFiscali, double deduzioni, double detrazioniFiscali) {
        double tasseDaVersare = (redditoAziendale * aliquoteFiscali) - (deduzioni + detrazioniFiscali);
        return tasseDaVersare;
    }

    public static double calcolaBudgetAnnuale(double venditePreviste, double costiProduzione, double margineProfitto, double speseOperative) {
        double budgetAnnuale = (venditePreviste - costiProduzione) * margineProfitto - speseOperative;
        return budgetAnnuale;
    }
    
    //query visualizzazione dati
    
      public static void pDip1(Connection conn){
        try{
            String query = "SELECT d.NomeDipartimento, COUNT(p.IdProgetto) AS NumeroProgetti " +
                        "FROM Dipartimento d " +
                        "LEFT JOIN Progetto p ON d.NomeDipartimento = p.Afferenza " +
                        "GROUP BY d.NomeDipartimento;";
            
            PreparedStatement pstmt = conn.prepareStatement(query);
                     
            ResultSet rs = pstmt.executeQuery();

                // Stampa dei risultati
                while (rs.next()) {
                    String nomeDipartimento = rs.getString("NomeDipartimento");
                    int numeroProgetti = rs.getInt("NumeroProgetti");
                    System.out.println("Dipartimento: " + nomeDipartimento + ", Numero Progetti: " + numeroProgetti);
                }
        }catch (SQLException ex) {
            System.out.print("Errore nella funzione:: " + ex);
        }
    }
    
    public static void pImp2(PreparedStatement pstmt) {
        try {
            String query = "SELECT D.NomeDipartimento AS Dipartimento, " +
                    "AVG(Q.SoddisfazioneAziendale) AS MediaSoddisfazioneAziendale, " +
                    "AVG(Q.SupportoColleghi) AS MediaSupportoColleghi, " +
                    "AVG(Q.ComunicazioneInterna) AS MediaComunicazioneInterna " +
                    "FROM Dipartimento D " +
                    "LEFT JOIN Impiegato I ON D.NomeDipartimento = I.Afferenza " +
                    "LEFT JOIN Questionario Q ON I.IdImpiegato = Q.Appartenenza " +
                    "GROUP BY D.NomeDipartimento;";
            ResultSet rs = pstmt.executeQuery(query);
    
            // Stampa dei risultati
            while (rs.next()) {
                String dipartimento = rs.getString("Dipartimento");
                double mediaSoddisfazioneAziendale = rs.getDouble("MediaSoddisfazioneAziendale");
                double mediaSupportoColleghi = rs.getDouble("MediaSupportoColleghi");
                double mediaComunicazioneInterna = rs.getDouble("MediaComunicazioneInterna");
                System.out.println("Dipartimento: " + dipartimento +
                        ", Media Soddisfazione Aziendale: " + mediaSoddisfazioneAziendale +
                        ", Media Supporto Colleghi: " + mediaSupportoColleghi +
                        ", Media Comunicazione Interna: " + mediaComunicazioneInterna);
            }
        } catch (SQLException ex) {
            System.out.print("Errore nella funzione: " + ex);
        }
    }
    
    public static void pPro3(Connection conn) {
        try {
            String query = "SELECT Fase, AVG(DAYS(CURRENT_DATE) - DAYS(DataInizio)) AS TempoMedio " +
                           "FROM FaseProgetto " +
                           "GROUP BY Fase;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Stampa dei risultati
            while (rs.next()) {
                String fase = rs.getString("Fase");
                int tempoMedio = rs.getInt("TempoMedio");
                System.out.println("Fase: " + fase + ", Tempo Medio: " + tempoMedio + " giorni");
            }
        } catch (SQLException ex) {
            System.out.print("Errore nella funzione: " + ex);
        }
    }
    
    public static void pPro1(Connection conn){
    try{
        String query = "SELECT e.IdProdotto, e.Versione, e.Guadagno * e.Vendite AS GuadagnoTotale " +
                    "FROM Evoluzione e;";
        
        PreparedStatement pstmt = conn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        // Stampa dei risultati
        while (rs.next()) {
            int idProdotto = rs.getInt("IdProdotto");
            String versione = rs.getString("Versione");
            double guadagnoTotale = rs.getDouble("GuadagnoTotale");
            System.out.println("Id Prodotto: " + idProdotto + ", Versione: " + versione + ", Guadagno Totale: " + guadagnoTotale);
        }
    }catch (SQLException ex) {
        System.out.print("Errore nella funzione: " + ex);
    }
    
}

    
    public static void pPro2(Connection conn){
        try{
            String query = "SELECT e.IdProdotto, e.Versione, e.Vendite - LAG(e.Vendite) OVER (PARTITION BY e.IdProdotto ORDER BY e.Versione) AS VariazioneVendite " +
                        "FROM Evoluzione e " +
                        "ORDER BY e.IdProdotto, e.Versione;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Stampa dei risultati
            while (rs.next()) {
                int idProdotto = rs.getInt("IdProdotto");
                String versione = rs.getString("Versione");
                double variazioneVendite = rs.getDouble("VariazioneVendite");
                System.out.println("Id Prodotto: " + idProdotto + ", Versione: " + versione + ", Variazione Vendite: " + variazioneVendite);
            }
        }catch (SQLException ex) {
            System.out.print("Errore nella funzione: " + ex);
        }
    }
    
    public static String pDip3(PreparedStatement pstmt){
        try {
            ResultSet rset = pstmt.executeQuery();
            int column_count = (rset.getMetaData()).getColumnCount();
            if (rset != null) {
                String outputS = "";
                String dipI = "";
                int s1n = 0;
                int s2n = 0;
                int s3n = 0;
                int s1p = 0;
                int s2p = 0;
                int s3p = 0;
                
                while (rset.next()) {
                    System.out.print(dipI+" - "+ rset.getString(1)+"\n");
                    if(!(dipI.equalsIgnoreCase(rset.getString(1)))){
                        dipI = rset.getString(1);
                        if(rset.getInt(2) >= 6)
                        {
                            s1p++;
                        }
                        else
                        {
                            s1n++;
                        }
                        
                        if(rset.getInt(3) >= 6)
                        {
                            s2p++;
                        }
                        else
                        {
                            s2n++;
                        }
                        
                        if(rset.getInt(4) >= 6)
                        {
                            s3p++;
                        }
                        else
                        {
                            s3n++;
                        }
                    }
                    else{
                        outputS += "Dipartimento:"+ dipI +" soddi"+ s1p+"\n";
                    }
                }
                return outputS;
            }
        } catch (SQLException ex) {
            System.out.print("Errore nella funzione: " + ex);
        }
        return "error";
    }
    
}
