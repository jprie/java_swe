package model;

import javafx.util.StringConverter;

public class PhotographerStringConverter extends StringConverter<Photographer> {

	@Override
	public String toString(Photographer ph) {
		if (ph != null)
			return ph.getFirstName() != null && ph.getLastName() != null ? ph.getFirstName() + " " + ph.getLastName()
					: null;
		else
			return null;
	}

	@Override
	public Photographer fromString(String string) {

		Photographer ph = null;
		if (string == null) {
			return ph;
		}

		int spaceIndex = string.indexOf(" ");

		if (spaceIndex == -1) {
			// no space found
			// treat as first name
			ph = new Photographer(0l, string, null);
		} else {
			String firstName = string.substring(0, spaceIndex);
			String lastName = string.substring(spaceIndex + 1);
			ph = new Photographer(0l, firstName, lastName);
		}

		return null;
	}

}
