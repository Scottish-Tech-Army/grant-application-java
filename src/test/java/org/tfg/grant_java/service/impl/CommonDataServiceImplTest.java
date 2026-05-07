package org.tfg.grant_java.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tfg.grant_java.dto.CommonDataDTO;
import org.tfg.grant_java.entity.CommonData;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.CommonDataRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonDataServiceImplTest {

    @Mock
    private CommonDataRepository commonDataRepository;

    @InjectMocks
    private CommonDataServiceImpl service;

    @Test
    void getCommonDataById_shouldReturnDto_whenFound() {
        // Arrange
        CommonData entity = new CommonData();
        entity.setId(10L);
        entity.setCharityId(1L);
        entity.setVersion("v1");
        entity.setDataJson("[{\"x\":1}]");
        entity.setTemplateTitle("T");
        entity.setDescription("D");
        entity.setIsActive(true);

        when(commonDataRepository.findById(10L)).thenReturn(Optional.of(entity));

        // Act
        CommonDataDTO dto = service.getCommonDataById(10L);

        // Assert
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getCharityId()).isEqualTo(1L);
        assertThat(dto.getVersion()).isEqualTo("v1");
        assertThat(dto.getDataJson()).isEqualTo("[{\"x\":1}]");
        assertThat(dto.getTemplateTitle()).isEqualTo("T");
        assertThat(dto.getDescription()).isEqualTo("D");
        assertThat(dto.getIsActive()).isTrue();

        verify(commonDataRepository).findById(10L);
        verifyNoMoreInteractions(commonDataRepository);
    }

    @Test
    void getCommonDataById_shouldThrowServiceException_whenNotFound() {
        // Arrange
        when(commonDataRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.getCommonDataById(999L))
                .isInstanceOf(ServiceException.class)
                .extracting(ex -> ((ServiceException) ex).getErrorCode())
                .isEqualTo(Constants.COMMON_DATA_ID_NOT_FOUND);

        verify(commonDataRepository).findById(999L);
        verifyNoMoreInteractions(commonDataRepository);
    }

    @Test
    void getAllCommonDataByCharityId_shouldReturnMappedDtos() {
        // Arrange
        CommonData c1 = new CommonData();
        c1.setId(1L); c1.setCharityId(5L); c1.setVersion("v1"); c1.setDataJson("[]"); c1.setIsActive(true);
        CommonData c2 = new CommonData();
        c2.setId(2L); c2.setCharityId(5L); c2.setVersion("v2"); c2.setDataJson("[]"); c2.setIsActive(false);

        when(commonDataRepository.findByCharityIdAndIsActiveTrue(5L)).thenReturn(List.of(c1, c2));

        // Act
        List<CommonDataDTO> result = service.getAllCommonDataByCharityId(5L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CommonDataDTO::getId).containsExactly(1L, 2L);
        assertThat(result).extracting(CommonDataDTO::getVersion).containsExactly("v1", "v2");

        verify(commonDataRepository).findByCharityIdAndIsActiveTrue(5L);
        verifyNoMoreInteractions(commonDataRepository);
    }

    @Test
    void getAllCommonDataByListOfIds_shouldReturnMappedDtos() {
        // Arrange
        CommonData c1 = new CommonData();
        c1.setId(11L); c1.setCharityId(5L); c1.setVersion("v1"); c1.setDataJson("[]"); c1.setIsActive(true);
        CommonData c2 = new CommonData();
        c2.setId(22L); c2.setCharityId(6L); c2.setVersion("v2"); c2.setDataJson("[]"); c2.setIsActive(true);

        when(commonDataRepository.findByIdIn(List.of(11L, 22L))).thenReturn(List.of(c1, c2));

        // Act
        List<CommonDataDTO> result = service.getAllCommonDataByListOfIds(List.of(11L, 22L));

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CommonDataDTO::getId).containsExactly(11L, 22L);

        verify(commonDataRepository).findByIdIn(List.of(11L, 22L));
        verifyNoMoreInteractions(commonDataRepository);
    }


    @Test
    void saveCommonData_shouldThrowServiceException_whenJsonInvalid() {
        // Arrange
        CommonDataDTO dto = new CommonDataDTO();
        dto.setCharityId(5L);
        dto.setVersion("v1");
        dto.setIsActive(true);
        dto.setDataJson("NOT_JSON");

        // Act + Assert
        assertThatThrownBy(() -> service.saveCommonData(dto))
                .isInstanceOf(ServiceException.class)
                .extracting(ex -> ((ServiceException) ex).getErrorCode())
                // matches your implementation (even though the constant name is odd for invalid JSON)
                .isEqualTo(Constants.COMMON_DATA_ID_NOT_FOUND);

        verifyNoInteractions(commonDataRepository);
    }
}