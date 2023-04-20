package it.isa.progetto;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for simple App.
 */
@RunWith(JUnitQuickcheck.class)
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    
    @InjectMocks
    private App apm;
    @Mock
    private AppTest service;
    @Mock
    private Connection conn;
    @Mock
    private Statement stmt;
    @Mock
    private PreparedStatement pstmt;
    @Mock
    private ResultSet rs;

    @BeforeEach
    public void initTest() {
        MockitoAnnotations.initMocks(this);
        
    }
    
    private int val_prec = 0;
    private int rv(){
        Random random = new Random();
        
        int randomNumber = random.nextInt(250);
        while(randomNumber == val_prec){
            randomNumber = random.nextInt(250);
        }
        val_prec = randomNumber;
        return randomNumber;
    }
    
    @Test
    public void testPDip1() throws Exception {
        String query = "SELECT d.NomeDipartimento, COUNT(p.IdProgetto) AS NumeroProgetti "
                + "FROM Dipartimento d " + "LEFT JOIN Progetto p ON d.NomeDipartimento = p.Afferenza "
                + "GROUP BY d.NomeDipartimento;";

        when(conn.prepareStatement(query)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); // Simulate one row in ResultSet
        when(rs.getString("NomeDipartimento")).thenReturn("TestDipartimento");
        when(rs.getInt("NumeroProgetti")).thenReturn(3);

        // Call the function to be tested
        apm.pDip1(conn);

        // Verify expected behavior
        verify(conn).prepareStatement(query);
        verify(pstmt).executeQuery();
        verify(rs).getString("NomeDipartimento");
        verify(rs).getInt("NumeroProgetti");
    }
    
    @Test
    public void testPImp2() throws Exception {
    String query = "SELECT D.NomeDipartimento AS Dipartimento, " +
    "AVG(Q.SoddisfazioneAziendale) AS MediaSoddisfazioneAziendale, " +
    "AVG(Q.SupportoColleghi) AS MediaSupportoColleghi, " +
    "AVG(Q.ComunicazioneInterna) AS MediaComunicazioneInterna " +
    "FROM Dipartimento D " +
    "LEFT JOIN Impiegato I ON D.NomeDipartimento = I.Afferenza " +
    "LEFT JOIN Questionario Q ON I.IdImpiegato = Q.Appartenenza " +
    "GROUP BY D.NomeDipartimento;";
    
    when(conn.prepareStatement(query)).thenReturn(pstmt);
    when(pstmt.executeQuery()).thenReturn(rs);
    when(rs.next()).thenReturn(true, false); // Simulate one row in ResultSet
    when(rs.getString("Dipartimento")).thenReturn("TestDipartimento");
    when(rs.getDouble("MediaSoddisfazioneAziendale")).thenReturn(4.5);
    when(rs.getDouble("MediaSupportoColleghi")).thenReturn(3.8);
    when(rs.getDouble("MediaComunicazioneInterna")).thenReturn(4.2);
    
    // Call the function to be tested
    apm.pImp2(conn);
    
    // Verify expected behavior
    verify(conn).prepareStatement(query);
    verify(pstmt).executeQuery();
    verify(rs).getString("Dipartimento");
    verify(rs).getDouble("MediaSoddisfazioneAziendale");
    verify(rs).getDouble("MediaSupportoColleghi");
    verify(rs).getDouble("MediaComunicazioneInterna");
    
    }

    @Test
    public void testPPro3() throws Exception {
        // Mock the query and result set
        String query = "SELECT Fase, AVG(DAYS(CURRENT_DATE) - DAYS(DataInizio)) AS TempoMedio " +
                       "FROM FaseProgetto " +
                       "GROUP BY Fase;";
        when(conn.prepareStatement(query)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);

        // Mock the ResultSet behavior
        when(rs.next()).thenReturn(true, false);
        when(rs.getString("Fase")).thenReturn("TestFase");
        when(rs.getInt("TempoMedio")).thenReturn(5);

        // Call the function to be tested
        apm.pPro3(conn);

        // Verify expected behavior
        verify(conn).prepareStatement(query);
        verify(pstmt).executeQuery();
        verify(rs).getString("Fase");
        verify(rs).getInt("TempoMedio");
    }

    @Test
    public void testPPro1() throws SQLException {

        // Mock the behavior of the database connection and result set
        String query = "SELECT e.IdProdotto, e.Versione, e.Guadagno * e.Vendite AS GuadagnoTotale " +
                "FROM Evoluzione e;";
        when(conn.prepareStatement(query)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); // Simulate one row in ResultSet
        when(rs.getInt("IdProdotto")).thenReturn(Integer.parseInt("1"));
        when(rs.getString("Versione")).thenReturn("1.0");
        when(rs.getDouble("GuadagnoTotale")).thenReturn(100.0);

        // Call the function to be tested
        apm.pPro1(conn);

        // Verify expected behavior
        verify(conn).prepareStatement(query);
        verify(pstmt).executeQuery();
        verify(rs).getInt("IdProdotto");
        verify(rs).getString("Versione");
        verify(rs).getDouble("GuadagnoTotale");
    }

    @Test
    public void testPPro2() throws SQLException {
        // Prepare test data
        int idProdotto = 1;
        String versione = "1.0";
        double variazioneVendite = 10.0;
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("IdProdotto")).thenReturn(idProdotto);
        when(rs.getString("Versione")).thenReturn(versione);
        when(rs.getDouble("VariazioneVendite")).thenReturn(variazioneVendite);
        when(conn.prepareStatement(Mockito.anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);

        // Call the function to be tested
        apm.pPro2(conn);

        // Verify expected behavior
        verify(conn).prepareStatement(Mockito.anyString());
        verify(pstmt).executeQuery();
        verify(rs).getInt("IdProdotto");
        verify(rs).getString("Versione");
        verify(rs).getDouble("VariazioneVendite");
        // Add any additional assertions based on your expected output or behavior
    }

    @Test
    public void testPDip3() throws Exception {
        String query = "SELECT d.NomeDipartimento, p.Soddisfazione1, p.Soddisfazione2, p.Soddisfazione3 "
                + "FROM Dipartimento d " + "LEFT JOIN Progetto p ON d.NomeDipartimento = p.Afferenza";
        when(conn.prepareStatement(query)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, true, false); // Simulate three rows in ResultSet
        when(rs.getString(1)).thenReturn("TestDipartimento");
        when(rs.getInt(2)).thenReturn(8); // Soddisfazione1 >= 6
        when(rs.getInt(3)).thenReturn(4); // Soddisfazione2 < 6
        when(rs.getInt(4)).thenReturn(7); // Soddisfazione3 >= 6

        // Call the function to be tested
        apm.pDip3(conn);

        // Verify expected behavior
        verify(conn).prepareStatement(Mockito.anyString());
        verify(pstmt).executeQuery();
        verify(rs, times(3)).getString(1); // Verify getString(1) is called at least 3 times
        verify(rs).getInt(2);
        verify(rs).getInt(3);
        verify(rs).getInt(4);

    }




    
    @Property
    public void prop_calcolaCostoPersonale(int numeroDipendenti, int stipendioMedio, int tasseLavoro){
        App a = new App();
        int x = a.calcolaCostoPersonale(numeroDipendenti, stipendioMedio, tasseLavoro);
        double y = numeroDipendenti * stipendioMedio;
        if(x != -1){
            assertTrue(y < x);
        }
    }

    @Test
    public void det_calcolaCostoPersonale(){
        App a = new App();
        int numeroDipendenti = rv();
        int stipendioMedio = rv();
        int tasseLavoro = rv();
        int x = a.calcolaCostoPersonale(numeroDipendenti, stipendioMedio, tasseLavoro);
        double y = numeroDipendenti * stipendioMedio;
        if(x != -1){
            assertTrue(y < x);
        }
    }


    @Property
    public void prop_calcolaTasse(int redditoAziendale, int aliquoteFiscali, int deduzioni, int detrazioniFiscali){
        App a = new App();
        int x = a.calcolaTasse(redditoAziendale,aliquoteFiscali,deduzioni,detrazioniFiscali);
        double y = redditoAziendale * aliquoteFiscali;
        if(x != -1){
            assertTrue(y <= x);
        }
    }

    @Test
    public void det_calcolaTasse(){
        App a = new App();
        int redditoAziendale = rv();
        int aliquoteFiscali = rv();
        int deduzioni = rv();
        int detrazioniFiscali = rv();
        int x = a.calcolaTasse(redditoAziendale,aliquoteFiscali,deduzioni,detrazioniFiscali);
        double y = redditoAziendale * aliquoteFiscali;
        if(x != -1){
            assertTrue(y > x);
        }
    }
    
    @Property
    public void prop_budgetAnnuale(int venditePreviste, int costiProduzione, int profittoLordo, int speseOperative){
        App a = new App();
        int x = a.calcolaBudgetAnnuale(venditePreviste,costiProduzione,profittoLordo,speseOperative);
        double y = venditePreviste * profittoLordo;
        if(x != -1){
            assertTrue(y > x);
        }
    }
    @Test
    public void det_budgetAnnuale(){
        App a = new App();
        int venditePreviste = rv();
        int costiProduzione = rv();
        int profittoLordo = rv();
        int speseOperative = rv();
        int x = a.calcolaBudgetAnnuale(venditePreviste,costiProduzione,profittoLordo,speseOperative);
        double y = venditePreviste * profittoLordo;
        if(x != -1){
            assertTrue(y > x);
        }
    }

    @Test
    public void det_calcolaStipendio(){
        App.ContestoStipendio c;
        LocalDate randomDate = LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(LocalDate.of(1950, 1, 1).toEpochDay(), LocalDate.of(2023, 1, 1).toEpochDay()));
        System.out.print("\nrd:"+randomDate);
        
        c = new App.ContestoStipendio(new App.cStipendioImpiegato());
        int x_imp = c.eseguiCalcolo(randomDate);
        if(x_imp != -1){
            assertTrue(x_imp >= 1200);
        }
        
        c = new App.ContestoStipendio(new App.cStipendioAmministratore());
        int x_amm = c.eseguiCalcolo(randomDate);
        if(x_amm != -1){
            assertTrue(x_amm >= 1600);
        }
        
        c = new App.ContestoStipendio(new App.cStipendioStagista());
        int x_sta = c.eseguiCalcolo(randomDate);
        if(x_sta != -1){
            assertTrue(x_amm >= 400);
        }

    }

    @Property
    public void prop_calcolaStipendio(LocalDate dataAssunzione) {
        
        App.ContestoStipendio c;
        
        c = new App.ContestoStipendio(new App.cStipendioImpiegato());
        int x_imp = c.eseguiCalcolo(dataAssunzione);
        if(x_imp != -1){
            assertTrue(x_imp >= 1200);
        }
        
        c = new App.ContestoStipendio(new App.cStipendioAmministratore());
        int x_amm = c.eseguiCalcolo(dataAssunzione);
        if(x_amm != -1){
            assertTrue(x_amm >= 1600);
        }
        
        c = new App.ContestoStipendio(new App.cStipendioStagista());
        int x_sta = c.eseguiCalcolo(dataAssunzione);
        if(x_sta != -1){
            assertTrue(x_amm >= 400);
        }

    }
}