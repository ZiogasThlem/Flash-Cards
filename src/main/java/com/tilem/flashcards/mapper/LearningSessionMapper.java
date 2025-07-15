package com.tilem.flashcards.mapper;

import com.tilem.flashcards.data.dto.LearningSessionDTO;
import com.tilem.flashcards.data.entity.LearningSession;
import com.tilem.flashcards.data.enums.YesNo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface LearningSessionMapper extends GenericMapper<LearningSession, LearningSessionDTO> {

	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "flashcardId", source = "flashcard.id")
	@Mapping(target = "isActive", source = "isActive", qualifiedByName = "yesNoToString")
	@Mapping(target = "repetitions", source = "repetitions")
	@Mapping(target = "easeFactor", source = "easeFactor")
	@Mapping(target = "interval", source = "interval")
	LearningSessionDTO toDto(LearningSession learningSession);

	@Mapping(target = "user", ignore = true)
	@Mapping(target = "flashcard", ignore = true)
	@Mapping(target = "isActive", source = "isActive", qualifiedByName = "stringToYesNo")
	@Mapping(target = "repetitions", source = "repetitions")
	@Mapping(target = "easeFactor", source = "easeFactor")
	@Mapping(target = "interval", source = "interval")
	LearningSession toEntity(LearningSessionDTO learningSessionDTO);

	@Mapping(target = "user", ignore = true)
	@Mapping(target = "flashcard", ignore = true)
	@Mapping(target = "isActive", source = "isActive", qualifiedByName = "stringToYesNo")
	@Mapping(target = "repetitions", source = "repetitions")
	@Mapping(target = "easeFactor", source = "easeFactor")
	@Mapping(target = "interval", source = "interval")
	@Mapping(target = "tempUuid", ignore = true)
	@Mapping(target = "tempUniqueID", ignore = true)
	void updateEntity(
			LearningSessionDTO learningSessionDTO, @MappingTarget LearningSession learningSession);

	@Named("yesNoToString")
	default String yesNoToString(YesNo yesNo) {
		return yesNo.name();
	}

	@Named("stringToYesNo")
	default YesNo stringToYesNo(String isActive) {
		return YesNo.valueOf(isActive);
	}
}