import com.research.exception.NotFoundException;
import com.research.model.Resident;
import com.research.repository.ResidentRepository;
import com.research.service.ResidentService;
import com.research.service.ValidationService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Epic("Resident Management")
@Feature("Resident Service")
@ExtendWith(MockitoExtension.class)
class ResidentServiceTest {

    @Mock
    private ResidentRepository residentRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private ResidentService residentService;

    private Resident resident;

    @BeforeEach
    void setup() {
        resident = new Resident(1, "Ahmed Ali",
                "ahmed@test.com", "+20123456789", "A-12");
    }

    @Test
    @Story("Add resident successfully")
    void shouldAddResident() {
        when(residentRepository.save(any())).thenReturn(resident);

        Resident result = residentService.addResident(
                1, "Ahmed Ali", "ahmed@test.com",
                "+20123456789", "A-12");

        assertEquals("Ahmed Ali", result.getFullName());
    }

    @Test
    @Story("Get resident not found")
    void shouldThrowNotFoundException() {
        when(residentRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> residentService.getResidentById(99));
    }
}
