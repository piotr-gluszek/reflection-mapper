import com.pgluszek.mapper.Mapper;
import com.pgluszek.mapper.MapperException;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class mapperTest {

    @Test
    public void mappingIsCorrect() {

        Http http = new Http("400", "Fatal error");
        Mapper mapper = new Mapper("src/test/resources/mapping.json");
        Http mappedHttp = null;
        try {
            mapper.loadMapping();
            mappedHttp = (Http) mapper.map(http).get();
            assertEquals("BAD REQUEST", mappedHttp.getStatus());
            assertEquals("Quite bad situation", mappedHttp.getMessage());
        } catch (NoSuchElementException | MapperException ex) {
            fail(ex.getMessage());
        }
    }

}
