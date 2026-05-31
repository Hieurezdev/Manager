package unit;

import com.example.manager.dao.HanhTrinhDAO;
import com.example.manager.entity.ChiTietHanhTrinh;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HanhTrinhDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private HanhTrinhDAO hanhTrinhDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        hanhTrinhDAO = new HanhTrinhDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testGetGaTrungGian_RouteExistsAndReturnsStations() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("maCTHT")).thenReturn("CTHT_01_HN", "CTHT_01_SG");
        when(mockResultSet.getInt("thuTuGa")).thenReturn(1, 4);
        when(mockResultSet.getString("maGa")).thenReturn("GA_HANOI", "GA_SAIGON");
        when(mockResultSet.getString("tenNhaGa")).thenReturn("Ga Hà Nội", "Ga Sài Gòn");

        List<ChiTietHanhTrinh> result = hanhTrinhDAO.getGaTrungGian("HT_HNSG");
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals("GA_HANOI", result.get(0).getNhaGa().getMaGa());
    }
}
