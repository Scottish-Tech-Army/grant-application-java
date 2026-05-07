package org.tfg.grant_java.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tfg.grant_java.dto.CharityDTO;
import org.tfg.grant_java.entity.Charity;
import org.tfg.grant_java.exception.ServiceException;
import org.tfg.grant_java.model.Constants;
import org.tfg.grant_java.repository.CharityRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharityServiceImplTest {

    @Mock
    private CharityRepository charityRepository;

    @InjectMocks
    private CharityServiceImpl service;

    @Test
    void getAllActiveCharities_shouldReturnMappedDtos_whenActiveCharitiesExist() {
        // Arrange
        Charity c1 = new Charity();
        c1.setId(1L);
        c1.setName("Charity One");
        c1.setIsActive(true);
        c1.setCreatedBy(String.valueOf(10));

        Charity c2 = new Charity();
        c2.setId(2L);
        c2.setName("Charity Two");
        c2.setIsActive(true);
        c2.setCreatedBy(String.valueOf(20));

        when(charityRepository.findAllByIsActiveTrue())
                .thenReturn(Optional.of(List.of(c1, c2)));

        // Act
        List<CharityDTO> result = service.getAllActiveCharities();

        // Assert
        assertThat(result).hasSize(2);

        CharityDTO dto1 = result.get(0);
        assertThat(dto1.getId()).isEqualTo(1L);
        assertThat(dto1.getName()).isEqualTo("Charity One");
        assertThat(dto1.getIsActive()).isTrue();
        assertThat(dto1.getCreatedBy()).isEqualTo("10");

        CharityDTO dto2 = result.get(1);
        assertThat(dto2.getId()).isEqualTo(2L);
        assertThat(dto2.getName()).isEqualTo("Charity Two");
        assertThat(dto2.getIsActive()).isTrue();
        assertThat(dto2.getCreatedBy()).isEqualTo("20");

        verify(charityRepository, times(1)).findAllByIsActiveTrue();
        verifyNoMoreInteractions(charityRepository);
    }

    @Test
    void getAllActiveCharities_shouldThrowServiceException_whenNoActiveCharities() {
        // Arrange: repository returns empty Optional -> should throw
        when(charityRepository.findAllByIsActiveTrue()).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.getAllActiveCharities())
                .isInstanceOf(ServiceException.class)
                .extracting(ex -> ((ServiceException) ex).getErrorCode())
                .isEqualTo(Constants.NO_ACTIVE_CHARITIES_AVAILABLE);

        verify(charityRepository, times(1)).findAllByIsActiveTrue();
        verifyNoMoreInteractions(charityRepository);
    }
}