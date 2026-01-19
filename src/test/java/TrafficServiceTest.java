import com.research.exception.BusinessRuleViolationException;
import com.research.model.*;
import com.research.repository.GatePassRepository;
import com.research.repository.VehicleRepository;
import com.research.repository.VisitReservationRepository;
import com.research.service.GateLaneService;
import com.research.service.TrafficService;
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

@Epic("Traffic Flow")
@Feature("Gate Pass Processing")
@ExtendWith(MockitoExtension.class)
class TrafficServiceTest {

    @Mock
    private GatePassRepository passRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GateLaneService laneService;

    @Mock
    private VisitReservationRepository reservationRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private TrafficService trafficService;

    private Vehicle vehicle;
    private GateLane lane;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle(1, "ABC-123", null, null, true);
        lane = new GateLane(1, 2, 5, LaneStatus.OPEN);
    }

    @Test
    @Story("Request entry successfully")
    void shouldRequestEntrySuccessfully() {
        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(laneService.getAvailableLane()).thenReturn(Optional.of(lane));
        when(passRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        GatePass pass = trafficService.requestEntry(1);

        assertEquals(PassDirection.ENTRY, pass.getDirection());
    }

    @Test
    @Story("Request entry denied for blocked vehicle")
    void shouldFailForBlockedVehicle() {
        vehicle.setAllowed(false);
        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));

        assertThrows(BusinessRuleViolationException.class,
                () -> trafficService.requestEntry(1));
    }
}
