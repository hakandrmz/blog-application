package guru.hakandurmaz.blog.utils.mappers;

import org.modelmapper.ModelMapper;

public interface ModelMapperService {
    ModelMapper forDto();
    ModelMapper forRequest();
}
