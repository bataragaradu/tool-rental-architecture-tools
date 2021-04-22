package com.rbinnovative.tools.service;

import com.rbinnovative.tools.exception.ToolException;
import com.rbinnovative.tools.model.dao.Tools;
import com.rbinnovative.tools.repository.ToolsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ToolAvailabilityService {

    private final ToolsRepository toolRepository;

    @Autowired
    public ToolAvailabilityService(ToolsRepository toolRepository){
        this.toolRepository = toolRepository;
    }

    public List<LocalDate> getAvailabilityForTool(Integer toolId) throws ToolException {
        Optional<Tools> toolRetrieved = toolRepository.findById(toolId);
        if (toolRetrieved.isPresent() && toolRetrieved.get().isAvailable()) {
            return extractAvailableDate();
        } else {
            throw new ToolException("The id doesn't exist, needs to be created " + toolId);
        }
    }

    private List<LocalDate> extractAvailableDate() {
        LocalDate localDateNow = LocalDate.now();
        LocalDate localDateEndOfCurrentMonth= localDateNow.withDayOfMonth(localDateNow.lengthOfMonth());
        int numberOfDaysBetween = localDateEndOfCurrentMonth.getDayOfMonth() - localDateNow.getDayOfMonth() + 1;
        return Stream.iterate(localDateNow, d -> d.plusDays(1))
                .limit(numberOfDaysBetween)
                .collect(Collectors.toList());
    }
}
