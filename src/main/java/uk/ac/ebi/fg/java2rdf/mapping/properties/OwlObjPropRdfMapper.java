package uk.ac.ebi.fg.java2rdf.mapping.properties;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import uk.ac.ebi.fg.java2rdf.mapping.BeanRdfMapper;
import uk.ac.ebi.fg.java2rdf.mapping.RdfMapperFactory;
import uk.ac.ebi.fg.java2rdf.mapping.RdfMappingException;
import uk.ac.ebi.fg.java2rdf.utils.OwlApiUtils;

/**
 * This maps a pair of Java objects by means of some OWL object type property. For instance, you may use this mapper
 * to map Book.getAuthor() via ex:has-author. Both the subject and object URI for the mapped RDF statement
 * (i.e., the book  and author's URIs) are taken from {@link RdfMapperFactory#getUri(Object, Map)}.
 * 
 * <dl><dt>date</dt><dd>Mar 24, 2013</dd></dl>
 * @author Marco Brandizi
 *
 */
public class OwlObjPropRdfMapper<T, PT> extends URIProvidedPropertyRdfMapper<T, PT>
{
	public OwlObjPropRdfMapper ()  {
		super ();
	}

	public OwlObjPropRdfMapper ( String targetPropertyUri ) {
		super ( targetPropertyUri );
	}
	
	
	/**
	 * Generates the RDF triple 
	 * ({@link #getMapperFactory() getMapperFactory(source, params)}, {@link #getTargetPropertyUri()}, 
	 *   {@link #getMapperFactory() getMapperFactory(propValue, params)} ).
	 */
	@Override
	public boolean map ( T source, PT propValue, Map<String, Object> params )
	{
		if ( propValue == null ) return false;
		try
		{
			RdfMapperFactory mapFactory = this.getMapperFactory ();
			
			// TODO: can we avoid to keep recomputing these
			//
			
			// This is necessary, cause source/pval may be swapped by InversePropRdfMapper
			String subjUri = mapFactory.getUri ( source, params );
			if ( subjUri == null ) return false;

			String objUri = mapFactory.getUri ( propValue, params );
			if ( objUri == null ) return false;
			
			OwlApiUtils.assertLink ( this.getMapperFactory ().getKnowledgeBase (), 
				subjUri, this.getTargetPropertyUri (), objUri );

			// Don't use targetMapper, we need to trace this visit.
			return mapFactory.map ( propValue, params );
		} 
		catch ( Exception ex )
		{
			throw new RdfMappingException ( String.format ( 
				"Error while doing the RDF mapping <%s[%s] '%s' [%s]: %s", 
					source.getClass ().getSimpleName (), 
					StringUtils.abbreviate ( source.toString (), 50 ), 
					this.getTargetPropertyUri (),
					StringUtils.abbreviate ( propValue.toString (), 50 ), 
					ex.getMessage ()
			), ex );
		}
	}
}
