package unit;

import com.example.manager.dao.VeTauDAO;
import com.example.manager.entity.VeTau;
import com.example.manager.enums.TrangThaiVe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class VeTauDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private VeTauDAO veTauDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        veTauDAO = new VeTauDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testSearchVeTau_TicketNotFound() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        VeTau ve = veTauDAO.searchVeTau("VE_INVALID");
        assertNull(ve);
    }

    @Test
    public void testSearchVeTau_TicketFoundAndStatusIsDaBan() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("maVe")).thenReturn("VE_0001");
        when(mockResultSet.getString("trangThai")).thenReturn("DaBan");
        
        VeTau ve = veTauDAO.searchVeTau("VE_0001");
        assertNotNull(ve);
        assertEquals(TrangThaiVe.DA_BAN, ve.getTrangThai());
    }

    @Test
    public void testSearchVeTau_TicketFoundButStatusIsDaHuy() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("maVe")).thenReturn("VE_0001");
        when(mockResultSet.getString("trangThai")).thenReturn("DaTra"); 
        
        VeTau ve = veTauDAO.searchVeTau("VE_0001");
        assertNotNull(ve);
        assertEquals(TrangThaiVe.DA_TRA, ve.getTrangThai());
    }

    @Test
    public void testUpdateTrangThai_TicketExistsUpdateStatusToDaHuy() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        boolean result = veTauDAO.updateTrangThai("VE_0001", TrangThaiVe.DA_TRA);
        assertTrue(result);
    }

    @Test
    public void testLayDanhSachVeTheoLichTrinh_HasSoldTickets() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("maVe")).thenReturn("VE_0001");
        when(mockResultSet.getString("trangThai")).thenReturn("DaBan");
        
        List<VeTau> result = veTauDAO.layDanhSachVeTheoLichTrinh("LT_001");
        assertFalse(result.isEmpty());
        assertEquals(TrangThaiVe.DA_BAN, result.get(0).getTrangThai());
    }

    @Test
    public void testLayDanhSachVeTheoLichTrinh_NoTicketsOrOnlyCanceled() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); 
        
        List<VeTau> result = veTauDAO.layDanhSachVeTheoLichTrinh("LT_001");
        assertTrue(result.isEmpty());
    }
}
