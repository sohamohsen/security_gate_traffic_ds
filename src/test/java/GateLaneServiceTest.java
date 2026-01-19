import com.research.exception.BusinessRuleViolationException;
import com.research.model.GateLane;
import com.research.model.LaneStatus;
import com.research.repository.GateLaneRepository;
import com.research.service.GateLaneService;
import com.research.service.ValidationService;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Epic("Traffic Management")
@Feature("Gate Lane Service")
@ExtendWith(MockitoExtension.class)
class GateLaneServiceTest {

    @Mock
    private GateLaneRepository laneRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private GateLaneService gateLaneService;

    private GateLane lane;

    @BeforeEach
    void setUp() {
        lane = new GateLane(1, 5, 10, LaneStatus.OPEN);
    }

    @Test
    @Story("Add lane successfully")
    @Severity(SeverityLevel.CRITICAL)
    void shouldAddLaneSuccessfully() {
        when(laneRepository.findByLaneNumber(5)).thenReturn(Optional.empty());
        when(laneRepository.save(any())).thenReturn(lane);

        GateLane result = gateLaneService.addLane(1, 5, 10);

        assertNotNull(result);
        assertEquals(5, result.getLaneNumber());
    }

    @Test
    @Story("Add duplicate lane")
    @Severity(SeverityLevel.NORMAL)
    void shouldFailWhenLaneNumberExists() {
        when(laneRepository.findByLaneNumber(5)).thenReturn(Optional.of(lane));

        assertThrows(BusinessRuleViolationException.class,
                () -> gateLaneService.addLane(1, 5, 10));
    }

    @Test
    @Story("Update lane status incorrectly")
    @Severity(SeverityLevel.MINOR)
    void shouldFailWhenChangingFromBusyDirectly() {
        lane.setStatus(LaneStatus.BUSY);

        when(laneRepository.findById(1)).thenReturn(Optional.of(lane));

        assertThrows(BusinessRuleViolationException.class,
                () -> gateLaneService.updateLaneStatus(1, LaneStatus.OPEN));
    }
}
