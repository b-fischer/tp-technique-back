package tp.bfischer.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PostController {
	
	private final static Logger LOGGER = Logger.getLogger(PostController.class);

	@RequestMapping("/posts")
	public List<Post> getPosts() throws IOException {

		List<Post> sortedPosts = new ArrayList<Post>();
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(
			"http://jsonplaceholder.typicode.com/posts");
		getRequest.addHeader("accept", "application/json");

		try {
			HttpResponse response = httpClient.execute(getRequest);
			String json = EntityUtils.toString(response.getEntity(), "UTF-8");
			ObjectMapper mapper = new ObjectMapper();
			List<Post> posts = mapper.readValue(json, new TypeReference<List<Post>>(){});

			
			
			sortedPosts = posts.stream()
					.limit(50)
					.sorted((post1, post2) -> post1.getTitle().compareTo(post2.getTitle()))
					.collect(Collectors.toList());
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage());
			throw e;
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw e;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		
		return sortedPosts;
	}
	
}
