package challenge.nDaysChallenge.service.dajim;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@JsonIgnoreProperties(value = {"pageable","sort"})
public class CustomSliceImpl<T> extends SliceImpl<T> {

    @JsonCreator
    public CustomSliceImpl(List<T> content, Pageable pageable, boolean hasNext) {
        super(content, pageable, hasNext);
    }

    @JsonGetter(value = "number")
    @Override
    public int getNumber() {
        return super.getNumber();
    }

    @JsonGetter(value = "size")
    @Override
    public int getSize() {
        return super.getSize();
    }

    @JsonGetter(value = "numberOfElements")
    @Override
    public int getNumberOfElements() {
        return super.getNumberOfElements();
    }

    @JsonGetter(value = "isFirst")
    @Override
    public boolean isFirst() {
        return super.isFirst();
    }

    @JsonGetter(value = "isLast")
    @Override
    public boolean isLast() {
        return super.isLast();
    }

}
