package unit;

import com.example.manager.dao.DoanTauDAO;
import com.example.manager.entity.DoanTau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DoanTauDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private DoanTauDAO doanTauDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        doanTauDAO = new DoanTauDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testGetAllDoanTau_ListOfTrainsIsNotEmpty() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("maTau")).thenReturn("SE1");
        when(mockResultSet.getString("tenTau")).thenReturn("Tàu Thống Nhất SE1");
        when(mockResultSet.getString("loaiTau")).thenReturn("TauNhanh");
        when(mockResultSet.getString("trangThai")).thenReturn("SanSang");
        when(mockResultSet.getInt("id")).thenReturn(1);

        List<DoanTau> result = doanTauDAO.getAllDoanTau();
        assertFalse(result.isEmpty());
        assertEquals("SE1", result.get(0).getMaTau());
    }
}
