package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Country;
import jrange.myordy.entity.State;
import jrange.myordy.entity.Suburb;

public interface SuburbDAO {

	public Suburb saveSuburb(Suburb suburb);
	public Suburb get(final Integer id);
	public Suburb get(final String code);
    public List<Suburb> listSuburbs();

    public State saveState(State state);
	public State getState(final Integer id);
    public List<State> listStates();
	public State getState(final Integer countryId, final String code);

    public Country saveCountry(Country country);
	public Country getCountry(final Integer id);
    public List<Country> listCountries();

    public List<Suburb> findSuburbs(final String countryCode, final String postcode, final Integer languageId);
}
