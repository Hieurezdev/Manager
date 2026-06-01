package unit;

import com.example.manager.dao.PhieuTraVeDAO;
import com.example.manager.entity.LichTrinh;
import com.example.manager.entity.VeTau;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class PhieuTraVeDAOTest {
    private PhieuTraVeDAO phieuTraVeDAO;

    @BeforeEach
    public void setUp() {
        phieuTraVeDAO = new PhieuTraVeDAO(null);
    }

    @Test
    public void testTinhTienPhat_ValidPenaltyCalculationBasedOnRules() {
        VeTau veTau = new VeTau();
        veTau.setGiaVe(1000000);
        LichTrinh lt = new LichTrinh();
        lt.setNgayKhoiHanh(LocalDateTime.now().plusHours(30)); // >= 24 hours -> 10%
        veTau.setLichTrinh(lt);
        
        int tienPhat = phieuTraVeDAO.tinhTienPhat(veTau);
        assertEquals(100000, tienPhat); // 10% cua 1000000
    }

    @Test
    public void testTinhTienPhat_InvalidPenaltyDueToLateCancellation() {
        VeTau veTau = new VeTau();
        veTau.setGiaVe(1000000);
        LichTrinh lt = new LichTrinh();
        // Sát giờ: Chỉ còn cách lúc chạy 2 tiếng (< 4 tiếng)
        lt.setNgayKhoiHanh(LocalDateTime.now().plusHours(2));
        veTau.setLichTrinh(lt);
        
        int tienPhat = phieuTraVeDAO.tinhTienPhat(veTau);
        assertEquals(-1, tienPhat);
    }
}
