package ca.com.arnon.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapper {

	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

	public static <S, D> D parseObject(S source, Class<D> destination) {
		return mapper.map(source, destination);
	}

	public static <S, D> List<D> parseListObjects(List<S> source, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();

		for (S s : source) {
			destinationObjects.add(mapper.map(s, destination));
		}
		return destinationObjects;
	}

}
