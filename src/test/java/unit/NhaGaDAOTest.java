package unit;

import com.example.manager.dao.NhaGaDAO;
import com.example.manager.entity.NhaGa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NhaGaDAOTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    private NhaGaDAO nhaGaDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        nhaGaDAO = new NhaGaDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    public void testLayDanhSachGa_CoDuLieu() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("maGa")).thenReturn("GA_HANOI");
        when(mockResultSet.getString("tenNhaGa")).thenReturn("Ga Hà Nội");
        when(mockResultSet.getString("diaChi")).thenReturn("Số 120 Lê Duẩn");
        when(mockResultSet.getString("soDienThoai")).thenReturn("02439423697");

        List<NhaGa> result = nhaGaDAO.layDanhSachGa();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void testTimKiemTheoTen_GanDung() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("maGa")).thenReturn("GA_HANOI", "GA_NAMDINH");
        when(mockResultSet.getString("tenNhaGa")).thenReturn("Ga Hà Nội", "Ga Nam Định");

        List<NhaGa> result = nhaGaDAO.timKiemTheoTen("Ga");
        assertEquals(2, result.size());
    }

    @Test
    public void testTimKiemTheoTen_ChuoiRong() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("maGa")).thenReturn("GA_HANOI");

        List<NhaGa> result = nhaGaDAO.timKiemTheoTen("");
        assertEquals(1, result.size());
    }

    @Test
    public void testTimKiemTheoTen_KhongThay() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        List<NhaGa> result = nhaGaDAO.timKiemTheoTen("Ga Hi");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCapNhatGa_ThieuThongTin() throws SQLException {
        boolean result = nhaGaDAO.capNhatGa(null, "Tên mới", "Địa chỉ", "0123");
        assertFalse(result);
    }

    @Test
    public void testTaoGaMoi_ThieuThongTin() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("NOT NULL constraint failed"));
        NhaGa result = nhaGaDAO.taoGaMoi(null, null, null);
        assertNull(result);
    }

    @Test
    public void testTimKiemTheoTen_NotFound() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        List<NhaGa> result = nhaGaDAO.timKiemTheoTen("Ga Không Tồn Tại");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTimKiemTheoTen_Found() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("maGa")).thenReturn("GA_HANOI");
        when(mockResultSet.getString("tenNhaGa")).thenReturn("Ga Hà Nội");
        when(mockResultSet.getString("diaChi")).thenReturn("Số 120 Lê Duẩn");
        when(mockResultSet.getString("soDienThoai")).thenReturn("02439423697");

        List<NhaGa> result = nhaGaDAO.timKiemTheoTen("Ga Hà Nội");
        assertEquals(1, result.size());
        assertEquals("GA_HANOI", result.get(0).getMaGa());
    }

    @Test
    public void testCapNhatGa_StationDoesNotExist() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);
        NhaGa ga = new NhaGa("GA_FAKE", "Ga Fake", "Dia Chi", "0123");
        boolean result = nhaGaDAO.capNhatGa(ga, "Ga Mới", "Dia Chi Moi", "0999");
        assertFalse(result);
    }

    @Test
    public void testCapNhatGa_StationExists() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        NhaGa ga = new NhaGa("GA_HANOI", "Ga Hà Nội", "Cũ", "0123");
        boolean result = nhaGaDAO.capNhatGa(ga, "Ga Hà Nội Mới", "Mới", "0999");
        assertTrue(result);
    }

    @Test
    public void testTaoGaMoi_ValidDataInsertSuccessfully() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        NhaGa ga = nhaGaDAO.taoGaMoi("Ga Mới", "Địa Chỉ Mới", "0123456789");
        assertNotNull(ga);
        assertEquals("Ga Mới", ga.getTenNhaGa());
    }

    @Test
    public void testXoaGa_StationExistsAndNotUsedInSchedule_Success() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); // kiemTraGaDangSuDung returns false
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // xoaGa returns 1
        NhaGa ga = new NhaGa("GA_HANOI", "Ga Hà Nội", "Cũ", "0123");
        boolean result = nhaGaDAO.xoaGa(ga);
        assertTrue(result);
    }

    @Test
    public void testXoaGa_StationCurrentlyUsedInSchedule_FailException() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // kiemTraGaDangSuDung returns true
        // xoaGa will return false immediately without executing executeUpdate
        NhaGa ga = new NhaGa("GA_HANOI", "Ga Hà Nội", "Cũ", "0123");
        boolean result = nhaGaDAO.xoaGa(ga);
        assertFalse(result);
    }
}
