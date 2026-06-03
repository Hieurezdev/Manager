package unit;

import com.example.manager.dao.QuanLyDAO;
import com.example.manager.entity.QuanLy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class QuanLyDAOTest {
    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    
    private QuanLyDAO quanLyDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        quanLyDAO = new QuanLyDAO(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    public void testCheckDangNhap_SaiMatKhauHoacTen() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        QuanLy ql = new QuanLy("sai_tai_khoan", "sai_mat_khau", "QuanLy", "HoatDong");
        boolean result = quanLyDAO.checkDangNhap(ql);
        assertFalse(result, "Phải trả về false khi sai tên đăng nhập hoặc mật khẩu");
    }

    @Test
    public void testCheckDangNhap_UsernameHoacPasswordRong() {
        QuanLy ql1 = new QuanLy("", "password123", "QuanLy", "HoatDong");
        QuanLy ql2 = new QuanLy("username123", "", "QuanLy", "HoatDong");

        assertFalse(quanLyDAO.checkDangNhap(ql1), "Phải trả về false khi username rỗng");
        assertFalse(quanLyDAO.checkDangNhap(ql2), "Phải trả về false khi password rỗng");
    }

    @Test
    public void testLayVaiTroDangNhap_VaiTroNhanVien() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("vaiTro")).thenReturn("NhanVien");
        
        String vaiTro = quanLyDAO.layVaiTroDangNhap("nhanvien", "123456");
        assertEquals("NhanVien", vaiTro, "Phải trả về đúng vai trò NhanVien");
    }

    @Test
    public void testLayVaiTroDangNhap_VaiTroQuanLy() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("vaiTro")).thenReturn("QuanLy");
        
        String vaiTro = quanLyDAO.layVaiTroDangNhap("quanly", "123456");
        assertEquals("QuanLy", vaiTro, "Phải trả về đúng vai trò QuanLy");
    }
}
