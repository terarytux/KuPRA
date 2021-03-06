package eu.komanda30.kupra.repositories;

import eu.komanda30.kupra.entity.Unit;

import org.springframework.data.repository.CrudRepository;

public interface Units extends CrudRepository<Unit, Integer> {
    Iterable<Unit> findByOrderByNameAsc();
    Unit findByName(String name);
    Unit findByAbbreviation(String abbreviation);
}
