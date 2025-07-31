package com.tilem.flashcards.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.tilem.flashcards.data.dto.DeckDTO;
import com.tilem.flashcards.data.entity.Deck;
import com.tilem.flashcards.mapper.DeckMapper;
import com.tilem.flashcards.repository.DeckRepository;
import com.tilem.flashcards.service.DeckService;
import com.tilem.flashcards.util.AppException;
import com.tilem.flashcards.util.LogWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeckServiceImpl extends GenericServiceImpl<Deck, DeckDTO, DeckRepository>
		implements DeckService {

	private final DeckMapper deckMapper;
    private static final LogWrapper log = LogWrapper.getLogger(MethodHandles.lookup().lookupClass());

	public DeckServiceImpl(DeckRepository deckRepo, DeckMapper deckMapper) {
		super(deckRepo, deckMapper, Deck.class);
		this.deckMapper = deckMapper;
    }

    @Override
    @Transactional
    public List<DeckDTO> importDecksFromFile(String fileContent) {
        List<DeckDTO> importedDecks = new ArrayList<>();
        try (CSVReader reader = new CSVReaderBuilder(new StringReader(fileContent)).build()) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                if (record.length > 0) {
	                DeckDTO deckDTO = new DeckDTO(null, record[0], null, null, null);
                    importedDecks.add(create(deckDTO));
                }
            }
        } catch (IOException | CsvException e) {
            log.error("Error processing CSV file for decks: " + e.getMessage());
            throw new AppException("Error reading CSV file for decks import.", e);
        }
        return importedDecks;
    }
}