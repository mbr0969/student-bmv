package edu.bmv.studentorder.validator.register;

import edu.bmv.studentorder.config.Config;
import edu.bmv.studentorder.domain.register.CityRegisterRequest;
import edu.bmv.studentorder.domain.register.CityRegisterResponse;
import edu.bmv.studentorder.domain.Person;
import edu.bmv.studentorder.exception.CityRegisterException;
import edu.bmv.studentorder.exception.TransportException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import static edu.bmv.studentorder.config.Config.CR_URL;

public class RealCityRegisterChecker  implements ICityRegisterChecker {

    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {

        CityRegisterRequest request = new CityRegisterRequest(person);
        Client client = ClientBuilder.newClient();
        CityRegisterResponse response = client.target(Config.CR_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON))
                .readEntity(CityRegisterResponse.class);

        return response;
    }
}
