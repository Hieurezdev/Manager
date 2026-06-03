package unit;

import com.example.manager.dao.GheNgoiDAO;
import com.example.manager.enums.TrangThaiGhe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GheNgoiDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    private GheNgoiDAO gheNgoiDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        gheNgoiDAO = new GheNgoiDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testUpdateTrangThai_SeatExistsUpdateStatusToTrong() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        boolean result = gheNgoiDAO.updateTrangThai("SE1_T1_G01", TrangThaiGhe.TRONG);
        assertTrue(result);
    }
}
