package unit;

import com.example.manager.dao.KhachHangDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class KhachHangDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private KhachHangDAO khachHangDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        khachHangDAO = new KhachHangDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testXacMinhKhachHang_ClientFound_IsNewFalse() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("maKH")).thenReturn("KH0001");
        
        String maKH = khachHangDAO.layMaKhachHangTheoCCCD("001201004567");
        assertEquals("KH0001", maKH); // Found
    }

    @Test
    public void testXacMinhKhachHang_ClientNotFound_IsNewTrue() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        String maKH = khachHangDAO.layMaKhachHangTheoCCCD("INVALID");
        assertEquals("", maKH); // Not found
    }
}
