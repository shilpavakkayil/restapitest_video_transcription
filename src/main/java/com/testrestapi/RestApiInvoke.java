package com.testrestapi;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class RestApiInvoke {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Transcript transcript = new Transcript();
		transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(transcript);
		System.out.println(jsonRequest);
		//creating http request
		HttpRequest request;
		try {
			request = HttpRequest.newBuilder()
			.uri(new URI("https://api.assemblyai.com/v2/transcript"))
			.header("Authorization", "99157896e6244bd5a63f704d8bc01dfe")
			.POST(BodyPublishers.ofString(jsonRequest))
			.build();
			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.body());
			transcript = gson.fromJson(response.body(), Transcript.class);
			HttpRequest getRequest = HttpRequest.newBuilder()
					.uri(new URI("https://api.assemblyai.com/v2/transcript/"+transcript.getId()))
					.header("Authorization", "99157896e6244bd5a63f704d8bc01dfe")
					.GET()
					.build();
			while(true) {
				HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
				transcript = gson.fromJson(getResponse.body(), Transcript.class);
				System.out.println(transcript.getStatus());
				if("completed".equals(transcript.getStatus())|| "error".equals(transcript.getStatus()))
				{
					break;
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Processing completed");
		System.out.println(transcript.getText());
		
		
	}

}
