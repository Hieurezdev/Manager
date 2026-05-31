package unit;

import com.example.manager.dao.HoaDonDAO;
import com.example.manager.entity.HoaDon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HoaDonDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    private HoaDonDAO hoaDonDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        hoaDonDAO = new HoaDonDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testCapNhatTrangThaiGhe_SeatIsAvailable_StatusTrongToTamGiu() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        boolean result = hoaDonDAO.capNhatTrangThaiGhe("SE1_T1_G01", "TAM_GIU");
        assertTrue(result);
    }

    @Test
    public void testCapNhatTrangThaiGhe_SeatIsAlreadyTakenByAnotherClient() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        boolean result = hoaDonDAO.capNhatTrangThaiGhe("SE1_T1_G01", "TAM_GIU");
        assertFalse(result);
    }

    @Test
    public void testLuuGiaoDichThanhToanThat_ValidTransactionDataSavedSuccessfully() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        HoaDon bill = new HoaDon();
        bill.setMaHoaDon("HD_0001");
        bill.setTongTien(1250000);
        
        assertDoesNotThrow(() -> {
            hoaDonDAO.luuGiaoDichThanhToanThat("NV_01", bill, "LT_SE1_20260527", "SE1_T1_G01", "KH0001");
        });
        
        verify(mockConnection, times(1)).commit();
    }
}
