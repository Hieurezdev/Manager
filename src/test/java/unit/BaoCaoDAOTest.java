package unit;

import com.example.manager.dao.BaoCaoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class BaoCaoDAOTest {
    private BaoCaoDAO baoCaoDAO;

    @BeforeEach
    public void setUp() {
        baoCaoDAO = new BaoCaoDAO(null);
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
}
