package com.felipe.fileuploader.services;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.felipe.fileuploader.daos.FileInfoDaoImpl;
import com.felipe.fileuploader.entities.FileInfo;
import com.felipe.fileuploader.enums.StatusUpload;
import com.felipe.fileuploader.util.DirUtil;

public class FileInfoServiceImplTest {

	FileInfoService service;

	@Before
	public void init() {
		service = new FileInfoServiceImpl();
	}

	@Test
	public void whenSavingFileInfoMustSucceed() {
		FileInfo info = generateFileInfo("test1", "unitTest",
				new Date().getTime(), StatusUpload.FINISHED);
		Boolean save = service.save(info);
		assertThat(save, is(equalTo(true)));
	}

	@Test
	public void whenSearchingByIdMustFetchEntity() {
		String id = "test1";
		String owner = "unitTest";
		Long initTimestamp = new Date().getTime();

		service.save(generateFileInfo(id, owner, initTimestamp,
				StatusUpload.FINISHED));

		FileInfo infoFetched = service.findById(id);

		assertThat(
				infoFetched,
				allOf(hasProperty("id", equalTo(id)),
						hasProperty("owner", equalTo(owner)),
						hasProperty("initTimestamp", equalTo(initTimestamp))));
	}

	@Test
	public void whenSearchingByIdMustFetchTheNewestOne() {
		String id = "test1";
		String owner = "unitTest";
		StatusUpload expectedStatus = StatusUpload.FINISHED;

		service.save(generateFileInfo("test1", "unitTest",
				new Date().getTime(), StatusUpload.PROGRESS));
		service.save(generateFileInfo("test1", "unitTest",
				new Date().getTime(), expectedStatus));

		FileInfo infoFetched = service.findById(id);

		assertThat(
				infoFetched,
				allOf(hasProperty("id", equalTo(id)),
						hasProperty("owner", equalTo(owner)),
						hasProperty("status", equalTo(expectedStatus.name()))));
	}
	
	@Test
	public void whenRetrievingAllInfoChunksByOwnerNameMustSucceed() {
		String owner = "felipe";
		String name = "test.txt";
		String id = owner + name;
		StatusUpload expectedStatus = StatusUpload.FINISHED;

		service.save(generateFileInfo(id, owner,
				new Date().getTime(), StatusUpload.PROGRESS));
		service.save(generateFileInfo(id, owner,
				new Date().getTime(), expectedStatus));

		List<FileInfo> chunkInfos = service.retrieveAllInfoChunksByOwnerName(owner, name);

		assertThat(chunkInfos, 
		        Matchers.<FileInfo>hasItem(
		            Matchers.hasProperty("id", Matchers.equalTo(id))));
		assertThat(chunkInfos.size(), is(equalTo(2)));
	}

	private FileInfo generateFileInfo(String id, String owner,
			Long initTimestamp, StatusUpload status) {
		FileInfo info = new FileInfo();
		info.setId(id);
		info.setOwner(owner);
		info.setInitTimestamp(initTimestamp);
		info.setStatus(status.name());

		return info;
	}

	@After
	public void eraseDataBase() {
		File file = new File(DirUtil.getDirDataBase()
				+ FileInfoDaoImpl.DATA_BASE_FILE_NAME);
		file.delete();
	}
}
