package unit;

import com.example.manager.dao.BaoCaoDAO;
import com.example.manager.entity.BaoCao;
import com.example.manager.entity.ChiTietBaoCao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BaoCaoDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    private BaoCaoDAO baoCaoDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        baoCaoDAO = new BaoCaoDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testKiemTraHopLe_ValidIntervalStartDateLessThanEndDate() {
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now();
        assertTrue(baoCaoDAO.kiemTraHopLe(start, end));
    }

    @Test
    public void testKiemTraHopLe_InvalidIntervalStartDateGreaterThanEndDate() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().minusDays(5);
        assertFalse(baoCaoDAO.kiemTraHopLe(start, end));
    }

    @Test
    public void testKiemTraHopLe_NullDates() {
        LocalDate start = LocalDate.now();
        assertFalse(baoCaoDAO.kiemTraHopLe(null, start));
        assertFalse(baoCaoDAO.kiemTraHopLe(start, null));
        assertFalse(baoCaoDAO.kiemTraHopLe(null, null));
    }

    @Test
    public void testTaoMoiBaoCao_SuccessAndInitialProperties() {
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now();
        BaoCao bc = baoCaoDAO.taoMoiBaoCao(start, end);
        assertNotNull(bc);
        assertEquals(0, bc.getTongDoanhThu());
        assertTrue(bc.getChiTietBaoCao() == null || bc.getChiTietBaoCao().isEmpty());
    }

    @Test
    public void testLuuKetQuaThongKe_Success() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.executeBatch()).thenReturn(new int[]{1});
        List<ChiTietBaoCao> dsKetQua = new ArrayList<>();
        ChiTietBaoCao ct = new ChiTietBaoCao("CT_01", 0, 0, 0, null);
        dsKetQua.add(ct);
        baoCaoDAO.luuKetQuaThongKe("BC_001", dsKetQua);
        verify(mockConnection, times(2)).prepareStatement(anyString());
    }
}
