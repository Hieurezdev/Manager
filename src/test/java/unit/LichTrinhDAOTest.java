package unit;

import com.example.manager.dao.LichTrinhDAO;
import com.example.manager.entity.DoanTau;
import com.example.manager.entity.HanhTrinh;
import com.example.manager.entity.LichTrinh;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LichTrinhDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private LichTrinhDAO lichTrinhDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        lichTrinhDAO = new LichTrinhDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testAddLichTrinh_ValidScheduleSavedSuccessfully() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        LichTrinh lt = new LichTrinh();
        DoanTau dt = new DoanTau();
        dt.setMaTau("SE1");
        lt.setDoanTau(dt);
        HanhTrinh ht = new HanhTrinh();
        ht.setMaHanhTrinh("HT_HNSG");
        lt.setHanhTrinh(ht);
        lt.setNgayKhoiHanh(LocalDateTime.now());
        
        boolean result = lichTrinhDAO.addLichTrinh(lt);
        assertTrue(result);
    }

    @Test
    public void testAddLichTrinh_TrainAlreadyHasOverlappingScheduleConflict() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Conflict overlapping schedule"));
        LichTrinh lt = new LichTrinh();
        DoanTau dt = new DoanTau();
        dt.setMaTau("SE1");
        lt.setDoanTau(dt);
        HanhTrinh ht = new HanhTrinh();
        ht.setMaHanhTrinh("HT_HNSG");
        lt.setHanhTrinh(ht);
        lt.setNgayKhoiHanh(LocalDateTime.now());
        
        boolean result = lichTrinhDAO.addLichTrinh(lt);
        assertFalse(result);
    }

    @Test
    public void testLayDanhSachChuyenTauPhuHop_ThereAreAvailableTrains() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getDouble("quangDuong")).thenReturn(1726.0);
        when(mockResultSet.getString("maLichTrinh")).thenReturn("LT_SE1_20260527");
        when(mockResultSet.getString("maTau")).thenReturn("SE1");
        when(mockResultSet.getString("gioDi")).thenReturn("19:30");
        when(mockResultSet.getString("gioDen")).thenReturn("21:00");
        
        List<LichTrinh> result = lichTrinhDAO.layDanhSachChuyenTauPhuHop("Ga Hà Nội", "Ga Sài Gòn", "2026-05-27");
        assertFalse(result.isEmpty());
    }

    @Test
    public void testLayDanhSachChuyenTauPhuHop_NoTrainAvailable() throws Exception {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        List<LichTrinh> result = lichTrinhDAO.layDanhSachChuyenTauPhuHop("Ga Hà Nội", "Ga Sài Gòn", "2026-05-27");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLayDanhSachLichTrinhTrongKy_RecordsFoundInTheInterval() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("maLichTrinh")).thenReturn("LT_SE1_20260527");
        when(mockResultSet.getTimestamp("ngayKhoiHanh")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(mockResultSet.getString("trangThai")).thenReturn("DaHoanThanh");
        
        List<LichTrinh> result = lichTrinhDAO.layDanhSachLichTrinhTrongKy("SE1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertFalse(result.isEmpty());
    }

    @Test
    public void testLayDanhSachLichTrinhTrongKy_NoRecordsFoundInTheInterval() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        
        List<LichTrinh> result = lichTrinhDAO.layDanhSachLichTrinhTrongKy("SE1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        assertTrue(result.isEmpty());
    }
}
