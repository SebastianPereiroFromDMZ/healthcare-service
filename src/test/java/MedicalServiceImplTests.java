import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTests {

    @Test
    void testCheckBloodPressure(){

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1"))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        SendAlertService alertService = Mockito.mock();


        BloodPressure currentPressure_WillBeCalled = new BloodPressure(60, 120);
        BloodPressure currentPressure_WillNotBeCalled = new BloodPressure(120,80);

        MedicalService medicalService_CallsTheMethod = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService_CallsTheMethod.checkBloodPressure("1", currentPressure_WillBeCalled);

        MedicalService medicalService_NonCallingMethod = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService_NonCallingMethod.checkBloodPressure("1", currentPressure_WillNotBeCalled);

        PatientInfo patientInfo = patientInfoRepository.getById("1");

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());

        Mockito.verify(alertService, Mockito.times(1)).send(message);
    }

    @Test
    void testCheckTemperature(){
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById("1"))
                .thenReturn(new PatientInfo("Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        SendAlertService alertService = Mockito.mock();

        BigDecimal currentTemperature = new BigDecimal("37.9");

        MedicalService medicalService_CallsTheMethod = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService_CallsTheMethod.checkTemperature("1", currentTemperature);

        PatientInfo patientInfo = patientInfoRepository.getById("1");

        String message = String.format("Warning, patient with id: %s, need help", patientInfo.getId());


        Mockito.verify(alertService, Mockito.times(0)).send(message);
    }
}
