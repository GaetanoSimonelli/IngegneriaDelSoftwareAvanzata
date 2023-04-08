package it.isa.progetto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;

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

    
    public static void pDip1(PreparedStatement pstmt){
        try{
            String query = "SELECT d.NomeDipartimento, COUNT(p.IdProgetto) AS NumeroProgetti " +
                        "FROM Dipartimento d " +
                        "LEFT JOIN Progetto p ON d.NomeDipartimento = p.Afferenza " +
                        "GROUP BY d.NomeDipartimento;";
            ResultSet rs = pstmt.executeQuery(query);

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
    
    public static void pPro1(PreparedStatement pstmt){
    try{
        String query = "SELECT e.IdProdotto, e.Versione, e.Guadagno * e.Vendite AS GuadagnoTotale " +
                    "FROM Evoluzione e;";
        ResultSet rs = pstmt.executeQuery(query);

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

    
    public static void pPro2(PreparedStatement pstmt){
        try{
            String query = "SELECT e.IdProdotto, e.Versione, e.Vendite - LAG(e.Vendite) OVER (PARTITION BY e.IdProdotto ORDER BY e.Versione) AS VariazioneVendite " +
                        "FROM Evoluzione e " +
                        "ORDER BY e.IdProdotto, e.Versione;";
            ResultSet rs = pstmt.executeQuery(query);

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
