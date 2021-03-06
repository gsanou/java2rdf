package uk.ac.ebi.fg.java2rdf.mapping.properties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import uk.ac.ebi.fg.java2rdf.mapping.CompositeObjRdfMapper;
import uk.ac.ebi.fg.java2rdf.mapping.RdfMapperFactory;
import uk.ac.ebi.fg.java2rdf.mapping.RdfMappingException;

/**
 * A composite property mapper. Similarly to {@link CompositeObjRdfMapper}, this uses the composite pattern to map 
 * a pair of java objects onto multiple {@link #getPropertyMappers() property mappers}. 
 *
 * <dl><dt>date</dt><dd>8 Aug 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class CompositePropRdfMapper<T, PT> extends PropertyRdfMapper<T, PT, Object>
{
	private List<PropertyRdfMapper<T, PT, ?>> propertyMappers;
	
	public CompositePropRdfMapper ()
	{
		this ( (PropertyRdfMapper<T, PT, ?>[]) null );
	}

	@SafeVarargs // TODO: value-by value param check
	public CompositePropRdfMapper ( PropertyRdfMapper<T, PT, ?> ... propertyMappers )
	{
		if ( propertyMappers == null ) return;
		this.setPropertyMappers ( Arrays.asList ( propertyMappers ) );
	}

	@SafeVarargs // TODO: value-by value param check
	public CompositePropRdfMapper ( String targetPropertyUri, UriProvidedPropertyRdfMapper<T, PT, ?> ... propertyMappers ) 
	{
		this ( propertyMappers );
		if ( propertyMappers == null ) return;
		for ( UriProvidedPropertyRdfMapper<T, PT, ?> pmapper: propertyMappers )
			pmapper.setTargetPropertyUri ( targetPropertyUri );
	}

	/**
	 * Similarly to {@link PropertyRdfMapper#map(Object, Object, Map)}, maps a pair of Java Objects, which are assumed to
	 * be related by a set of binary relations, to a set of RDF statements that represent such relations. Does this by
	 * invoking each of the {@link #getPropertyMappers()} this mapper has been configured with. 
	 * 
	 */
	@Override
	public boolean map ( T source, PT propValue, Map<String, Object> params )
	{
		if ( !super.map ( source, propValue, params ) ) return false;
		if ( propertyMappers == null || propertyMappers.isEmpty () ) throw new RdfMappingException ( String.format ( 
			"Internal error: cannot map [%s, %s] to RDF without property mappers", 
			StringUtils.abbreviate ( source.toString (), 30 ),
			StringUtils.abbreviate ( propValue.toString (), 30 )
		));
		
		boolean result = false;
		for ( PropertyRdfMapper<T, PT, ?> mapper: this.propertyMappers )
			result |= mapper.map ( source, propValue, params );

		return result;
	}

	/**
	 * The property mappers used by {@link #map(Object, Object, Map)} to map a Java object pairs into multiple binary
	 * relations and corresponding multiple RDF statements.
	 */
	public List<PropertyRdfMapper<T, PT, ?>> getPropertyMappers ()
	{
		return propertyMappers;
	}

	public void setPropertyMappers ( List<PropertyRdfMapper<T, PT, ?>> propertyMappers )
	{
		this.propertyMappers = propertyMappers;
	}

	/**
	 * Sets its own factory and also the factory of {@link #getPropertyMappers()}.
	 */
	@Override
	public void setMapperFactory ( RdfMapperFactory mapperFactory )
	{
		super.setMapperFactory ( mapperFactory );
		if ( this.propertyMappers == null ) return;
		
		for ( PropertyRdfMapper<T, PT, ?> pmapper: this.propertyMappers )
			pmapper.setMapperFactory ( mapperFactory );
	}
	
}
