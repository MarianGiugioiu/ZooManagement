package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.DietRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DietService{
    private final DietRepository dietRepository;


}
