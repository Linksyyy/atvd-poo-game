import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuardiaoTest {

    @Test
    void testConstructorSetsCorrectPvAndPvMax() {
        Guardiao guardiao = new Guardiao("Arthur");
        assertEquals(120, guardiao.getPv(), "Guardiao's initial PV should be 120.");
        assertEquals(120, guardiao.getPvMax(), "Guardiao's initial PV Max should be 120.");
    }

    @Test
    void testAtacarDamageRange() {
        Guardiao guardiao = new Guardiao("Arthur");
        int numTests = 1000;

        for (int i = 0; i < numTests; i++) {
            int damage = guardiao.atacar();
            assertTrue(damage >= 10, "Damage should be at least 10.");
            assertTrue(damage <= 10 + 5, "Damage should not exceed 10+5 = 15.");
        }
    }
}