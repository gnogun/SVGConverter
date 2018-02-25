$(document).ready(function() {
	initTree();
	addButtonEvent();
});

function addButtonEvent() {
	$("#zipUploadButton").click(function() {
		// resetModal();
		zipUpload();
	});
	$("#newFolderButton").click(function() {
		// resetModal();
		newFolder();
	});

	$("#reNameButton").click(function() {
		// resetModal();
		reName();
	});

	$("#svgExportButton, #svgExportMultiButton").click(function() {
		// resetModal();
		svgExport();
	});

	$("#exportMultiSvg").click(function() {
		$("#svgExportModal .currentPath").val("");
		$("#svgExportMultiModal").modal();
	});

	$("#createMultiCsvButton").click(function() {
		// resetModal();
		$("#createCsvModal").modal();
	});

	$("#createCsvButton").click(function() {
		var list = new Array();
		var select = $("#treeSelect p");

		for (var i = 0; i < select.size(); i++) {
			list.push(select.eq(i).text());
		}

		createCsv(list);
	});

	$("#createTranslatedSvgButton").click(function() {
		// resetModal();
		$("#translateSvgModal").modal();
	});

	$("#translateFileUploadButton").click(function() {
		translateSvg();
	});

	$("#createDirLinkInfoButton").click(function() {
		resetModal();
		loadLinkPath();
		loadSelector();
		$("#createLinkInfoModal").modal();

	});

	$("#createLinkInfoButton").click(function() {

		createLinkInfo();
	});
	
	$("#addSelectorButton").click(function() {
		var editVal = $("#editSelector").val();
		editVal.replace(/ /g, '');
		addSelector(editVal);
	});
	
	$("#deleteSelectorButton").click(function() {
		var editVal = $("#editSelector").val();
		editVal.replace(/ /g, '');
		deleteSelector(editVal);
	});
	
	

}

function initTree() {
	$("#tree").fancytree(
			{
				extensions : [ "contextMenu" ],
				source : {
					url : "tree"
				},
				contextMenu : {
					menu : function(node) {
						if (node.getKeyPath() == "/") {
							return {
								"uploadZip" : {
									"name" : "SVG ZIP 업로드"
								},
								"sep1" : "---------",
								"newfolder" : {
									"name" : "새 폴더"
								},
								"rename" : {
									"name" : "이름 변경",
									"disabled" : true
								},
								"delete" : {
									"name" : "삭제",
									"disabled" : true
								},
								"sep2" : "---------",
								"createcsv" : {
									"name" : "SVG 텍스트 생성",
									"disabled" : true
								},
								"exportimg" : {
									"name" : "EPS, GIF 생성",
									"disabled" : true
								}
							};
						} else if (node.folder) {
							return {
								"uploadZip" : {
									"name" : "SVG ZIP 업로드"
								},
								"sep1" : "---------",
								"newfolder" : {
									"name" : "새 폴더"
								},
								"rename" : {
									"name" : "이름 변경"
								},
								"delete" : {
									"name" : "삭제"
								},
								"sep2" : "---------",
								"createcsv" : {
									"name" : "SVG 텍스트 생성",
									"disabled" : true
								},
								"exportimg" : {
									"name" : "EPS, GIF 생성",
									"disabled" : true
								}
							};
						} else {
							return {
								"uploadZip" : {
									"name" : "SVG ZIP 업로드",
									"disabled" : true
								},
								"sep1" : "---------",
								"newfolder" : {
									"name" : "새 폴더",
									"disabled" : true
								},
								"rename" : {
									"name" : "이름 변경"
								},
								"delete" : {
									"name" : "삭제"
								},
								"sep2" : "---------",
								"createcsv" : {
									"name" : "SVG 텍스트 생성"
								},
								"exportimg" : {
									"name" : "EPS, GIF 생성"
								}
							}
						}

					},
					/*
					 * 
					 * function(node){ if (node.folder){ return { 'create':{
					 * 'name': 'folder option', 'icon': 'paste'} }; } else {
					 * return { 'create':{ 'name': 'leaf option', 'icon':
					 * 'paste'} }; }
					 */
					actions : function(node, action, options) {

						if (action == "uploadZip") {
							resetModal();
							$("#zipUploadModal .modal-title").text(
									"SVG ZIP 업로드 - /root" + node.getKeyPath());
							$("#zipUploadModal").modal();
							$("#zipUploadModal .currentPath").val(
									node.getKeyPath());
						} else if (action == "newfolder") {
							resetModal();
							$("#newFolderModal .modal-title").text(
									"새 폴더 - /root" + node.getKeyPath());
							$("#newFolderModal").modal();
							$("#newFolderModal .currentPath").val(
									node.getKeyPath());
						} else if (action == "rename") {
							resetModal();
							$("#reNameModal .modal-title").text(
									"파일/폴더 이름 변경 - /root" + node.getKeyPath());
							$("#reNameModal").modal();
							$("#reNameModal .currentPath").val(
									node.getKeyPath());
						} else if (action == "delete") {
							if (confirm("(" + node.getKeyPath()
									+ ") 삭제 하시겠습니까?")) {
								remove(node.getKeyPath());

							}

						} else if (action == "createcsv") {
							var list = new Array();
							if (node.folder) {

							} else {
								list.push(node.getKeyPath());
								createCsv(list);
							}

						} else if (action == "exportimg") {
							resetModal();
							$("#svgExportModal .modal-title").text(
									"SVG 이미지 변환 - /root" + node.getKeyPath());
							$("#svgExportModal").modal();
							$("#svgExportModal .currentPath").val(
									node.getKeyPath());
						}
						/*
						 * alert( "Selected action '" + action + "' on node " +
						 * node + ".");
						 */
					}
				},
				lazyLoad : function(event, data) {
					data.result = $.ajax({
						url : "ajax-sub2.json",
						dataType : "json"
					});
				}
			});

	$("#csvTree").fancytree({
		source : {
			url : "tree"
		},
		checkbox : true,
		selectMode : 3,
		lazyLoad : function(event, data) {
			data.result = $.ajax({
				url : "ajax-sub2.json",
				dataType : "json"
			});
		},
		select : function(event, data) {
			// Get a list of all selected nodes, and convert to a key array:
			var selKeys = $.map(data.tree.getSelectedNodes(), function(node) {
				return node.getKeyPath();
			});
			// $("#treeSelect").html(selKeys.join("<br/> "));

			// Get a list of all selected TOP nodes
			var selRootNodes = data.tree.getSelectedNodes(true);
			// ... and convert to a key array:
			var selRootKeys = $.map(selRootNodes, function(node) {
				return node.getKeyPath();
			});

			var text = "";

			for (var i = 0; i < selKeys.length; i++) {
				var title = selKeys[i].replace("//", "/");
				text += "<p>" + title + "</p>";
			}

			$("#treeSelect").html(text);
			// $("#echoSelectionRoots3").text(selRootNodes.join(", "));
		}
	});

	$("#exportTree").fancytree({
		source : {
			url : "tree"
		},
		checkbox : true,
		selectMode : 3,
		lazyLoad : function(event, data) {
			data.result = $.ajax({
				url : "ajax-sub2.json",
				dataType : "json"
			});
		},
		select : function(event, data) {
			// Get a list of all selected nodes, and convert to a key array:
			var selKeys = $.map(data.tree.getSelectedNodes(), function(node) {
				return node.getKeyPath();
			});
			// $("#treeSelect").html(selKeys.join("<br/> "));

			// Get a list of all selected TOP nodes
			var selRootNodes = data.tree.getSelectedNodes(true);
			// ... and convert to a key array:
			var selRootKeys = $.map(selRootNodes, function(node) {
				return node.getKeyPath();
			});

			var text = "";

			for (var i = 0; i < selKeys.length; i++) {
				var title = selKeys[i].replace("//", "/");
				text += "<p>" + title + "</p>";
			}

			$("#exportTreeSelect").html(text);
			// $("#echoSelectionRoots3").text(selRootNodes.join(", "));
		}
	});

	/*
	 * $("#linkTree").fancytree( { extensions : [ "contextMenu" ], source : {
	 * url : "tree/dir" }, contextMenu : { menu : function(node) { return {
	 * "sdPath" : { "name" : "SD 경로 선택" }, "ccPath" : { "name" : "CC 경로 선택" },
	 * "clPath" : { "name" : "CL 경로 선택" }, "hlPath" : { "name" : "HL 경로 선택" } }; },
	 * 
	 * 
	 * function(node){ if (node.folder){ return { 'create':{ 'name': 'folder
	 * option', 'icon': 'paste'} }; } else { return { 'create':{ 'name': 'leaf
	 * option', 'icon': 'paste'} }; }
	 * 
	 * actions : function(node, action, options) {
	 * 
	 * if (action == "sdPath") { $("#sdPath").text(node.getKeyPath()); } else if
	 * (action == "ccPath") { $("#ccPath").text(node.getKeyPath()); } else if
	 * (action == "clPath") { $("#clPath").text(node.getKeyPath()); } else if
	 * (action == "hlPath") { $("#hlPath").text(node.getKeyPath()); } } },
	 * lazyLoad : function(event, data) { data.result = $.ajax({ url :
	 * "ajax-sub2.json", dataType : "json" }); } });
	 */
}

function zipUpload() {
	var file = $("#svgZipInput").val();
	addLog(file + " - svg zip 업로드...");

	if (file == null || file == "") {
		alert("파일을 선택해 주세요.");
	} else {
		var extension = getExtension(file);

		if (confirm("경로에 같은 이름, 폴더는 업로드 된 파일로 변경 됩니다. 업로드 하시겠습니까?")) {

			if (extension != "zip") {
				alert("zip 압축 파일을 선택해 주세요.");
				addLog("zip 압축 파일을 선택해 주세요.");
			} else {

				var form = new FormData(document.getElementById('svgZipForm'));

				// $("#zipUploadButton").attr('disabled','disabled');

				$("#zipUploadButton").button("loading");

				$.ajax({
					url : "upload/svgZip",
					data : form,
					dataType : 'text',
					type : 'POST',
					processData : false,
					contentType : false,
					success : function(response, status, xhr) {
						if (status == "success") {
							// document.location.reload();
							initTree();
							resetModal();
							addLog(file + " 업로드 완료");
							$('.modal').modal('hide');
							// $("#zipUploadButton").removeAttr('disabled');
							$("#zipUploadButton").button("reset");

						} else {
							alert("파일 전송 오류");
						}
					},
					error : function(request, status, error) {

					},
					complete : function(jqXHR, textStatus) {

					}
				});
			}

		}
	}
}

function newFolder() {
	var currentPath = $("#newFolderModal .currentPath").val();
	var folderName = $("#newFolderInput").val();

	if (folderName.trim() == "") {
		alert("폴더 명을 입력해 주세요.");
	} else {
		$.ajax({
			url : "create/folder",
			dataType : 'text',
			type : 'POST',
			data : currentPath + "/" + folderName,
			processData : false,
			contentType : false,
			success : function(response, status, xhr) {
				if (status == "success") {
					// document.location.reload();
					initTree();
					resetModal();
					addLog(currentPath + " 경로에 새 폴더 " + folderName + " 생성");

				} else {
					alert(response);
				}
			},
			error : function(request, status, error) {

			},
			complete : function(jqXHR, textStatus) {

			}
		});
	}

}

function reName() {
	var currentPath = $("#reNameModal .currentPath").val();
	var newName = $("#reNameInput").val();

	if (newName.trim() == "") {
		alert("이름을 입력해 주세요.");
	} else {

		var data = {
			oldPath : currentPath,
			newName : newName
		}

		$.ajax({
			url : "rename/",
			data : JSON.stringify(data),
			dataType : 'text',
			type : 'POST',
			processData : false,
			contentType : "application/json",
			success : function(response, status, xhr) {
				if (status == "success") {
					// document.location.reload();
					initTree();
					resetModal();
					addLog(currentPath + " 경로 이름 " + newName + " 로 변경");
				} else {
					alert(response);
				}
			},
			error : function(request, status, error) {

			},
			complete : function(jqXHR, textStatus) {

			}
		});
	}
}

function remove(keyPath) {
	$.ajax({
		url : "delete",
		dataType : 'text',
		data : keyPath,
		type : 'POST',
		processData : false,
		contentType : false,
		success : function(response, status, xhr) {
			if (status == "success") {
				// document.location.reload();
				initTree();
				resetModal();
				addLog(keyPath + " 삭제");
			} else {
				alert(response);
			}
		},
		error : function(request, status, error) {

		},
		complete : function(jqXHR, textStatus) {

		}
	});
}

function createCsv(keyList) {

	var data = {
		svgFiles : keyList
	}

	$
			.ajax({
				url : "create/csv",
				dataType : 'text',
				data : JSON.stringify(data),
				type : 'POST',
				processData : false,
				contentType : "application/json",
				success : function(response, status, xhr) {
					if (status == "success") {
						// resetModal();
						$("#createCsvModal").modal();
						// $("#downloadModal").modal();
						$("#createCsvModal .fileDownload").show();
						$("#createCsvModal .fileDownload").attr("href",
								"download/" + response + "/");
						$("#createCsvModal .fileDownload").text(
								"Download " + response);

						// document.location.reload();
					} else {
						// alert(response);
					}
				},
				error : function(request, status, error) {

				},
				complete : function(jqXHR, textStatus) {

				}
			});
}

function svgExport() {

	var path = $("#exportTreeSelect p");

	var list = new Array();
	if (path.size() == 0) {
		var currentPath = $("#svgExportModal .currentPath").val();
		if (currentPath != "" && currentPath != undefined
				&& currentPath != null) {
			list.push(currentPath);
		}
	} else {
		for (var i = 0; i < path.size(); i++) {
			list.push(path.eq(i).text());
		}
	}
	var flag = true;

	if (list.length == 0) {
		alert("SVG 파일을 선택해주세요");
		flag = false;
	}

	var data = {
		svgFiles : list,
		gifFlag : $(".in #gifFlag").is(":checked"),
		epsFlag : $(".in #epsFlag").is(":checked"),
		sepGifFlag : $(".in #sepGifFlag").is(":checked"),
		sepEpsFlag : $(".in #sepEpsFlag").is(":checked"),
	}

	if (!data["gifFlag"] && !data["epsFlag"] && !data["sepGifFlag"]
			&& !data["sepEpsFlag"]) {
		alert("변환 옵션을 선택해 주세요");
		flag = false;
	}

	if (flag) {

		$.ajax({
			url : "export",
			dataType : 'text',
			data : JSON.stringify(data),
			type : 'POST',
			processData : false,
			contentType : "application/json",
			success : function(response, status, xhr) {
				if (status == "success") {
					resetModal();
					// $("#downloadModal").modal();
					// $("#fileDownload").attr("href", "download/" + response);

					$("#svgExportModal .fileDownload").show();
					$("#svgExportModal .fileDownload").attr("href",
							"download/" + response + "/");
					$("#svgExportModal .fileDownload").text(
							"Download " + response);

					$("#svgExportMultiModal .fileDownload").show();
					$("#svgExportMultiModal .fileDownload").attr("href",
							"download/" + response + "/");
					$("#svgExportMultiModal .fileDownload").text(
							"Download " + response);

					// document.location.reload();
				} else {
					// alert(response);
				}
			},
			error : function(request, status, error) {

			},
			complete : function(jqXHR, textStatus) {

			}
		});
	}

}

function translateSvg() {
	var file = $("#translateFile").val();
	addLog(file + " - 번역 csv 업로드...");

	if (file == null || file == "") {
		alert("파일을 선택해 주세요.");
	} else {
		var extension = getExtension(file);

		if (extension != "csv") {
			alert("번역 csv 파일을 선택해 주세요.");
			addLog("번역 csv 파일을 선택해 주세요.");
		}

		var form = new FormData(document.getElementById('translateSvgForm'));

		$.ajax({
			url : "translate/svg",
			data : form,
			dataType : 'text',
			type : 'POST',
			processData : false,
			contentType : false,
			success : function(response, status, xhr) {
				if (status == "success") {
					resetModal();
					// $("#downloadModal").modal();

					$("#translateSvgModal .fileDownload").show();
					$("#translateSvgModal .fileDownload").attr("href",
							"download/" + response + "/");
					$("#translateSvgModal .fileDownload").text(
							"Download " + response);

				} else {
					alert("파일 전송 오류");
				}
			},
			error : function(request, status, error) {

			},
			complete : function(jqXHR, textStatus) {

			}
		});
	}

}

function setLinkPath(target, path) {
	switch (target) {
	case "sd":
		$("#sdPath").text(path);
	case "cc":
		$("#ccPath").text(path);
	case "cl":
		$("#clPath").text(path);
	case "hl":
		$("#hlPath").text(path);
	}
}

function createLinkInfo() {

	$("#createLinkInfoButton").button("loading");

	var data = {
		sdPath : $("#sdPath option:checked").val(),
		ccPath : $("#ccPath option:checked").val(),
		clPath : $("#clPath option:checked").val(),
		hlPath : $("#hlPath option:checked").val(),
		selector : $("#linkSelector option:checked").val()
	};

	var flag = true;

	if (data["sdPath"] == "-") {
		alert("SD 경로를 선택해 주세요");
		flag = false;
	}

	if (data["ccPath"] == "-") {
		alert("CC 경로를 선택해 주세요");
		flag = false;
	}

	if (data["clPath"] == "-") {
		flag = false;
		alert("CL 경로를 선택해 주세요");
	}

	if (data["hlPath"] == "-") {
		flag = false;
		alert("HL 경로를 선택해 주세요");
	}

	var pathSelect = $(".linkPath");

	var result = [];
	for ( var key in data) { // 배열의 원소수만큼 반복

		if ($.inArray(data[key], result) == -1) { // result 에서 값을 찾는다. //값이
			// 없을경우(-1)
			result.push(data[key]); // result 배열에 값을 넣는다.
		}

	}
	;

	if (result.length < 5) {
		alert("경로가 중복 선택 되었습니다.");
		flag = false;
	}

	if (flag) {
		$.ajax({
			url : "create/link",
			dataType : 'text',
			data : JSON.stringify(data),
			type : 'POST',
			processData : false,
			contentType : "application/json",
			success : function(response, status, xhr) {
				if (status == "success") {
					$("#createLinkInfoButton").button("reset");
					// resetModal();
					// $("#downloadModal").modal();
					/*
					 * $("#fileDownload").attr("href", "download/" + response +
					 * "/");
					 */
					$("#createLinkInfoModal .fileDownload").show();
					$("#createLinkInfoModal .fileDownload").attr("href",
							"download/" + response + "/");
					$("#createLinkInfoModal .fileDownload").text(
							"Download " + response);

					// document.location.reload();
				} else {
					// alert(response);
				}
			},
			error : function(request, status, error) {

			},
			complete : function(jqXHR, textStatus) {

			}
		});
	}

}

function addLog(message) {
	$("#console").append("<p>" + message + "</p>");
}

function getExtension(file) {
	var extension = file.replace(/^.*\./, '');

	if (extension == file) {
		extension = '';
	} else {
		extension = extension.toLowerCase();
	}

	return extension;
}

function resetModal() {
	// $('.modal').modal('hide');

	$(".modal input[type='text']").val("");

	$(".modal .linkPath").text("");

	$(".modal .fileDownload").text("");

	$(".modal .fileDownload").attr("href", "#");
	$(".modal .fileDownload").hide();

}

function loadLinkPath() {
	$.ajax({
		url : "dir",
		dataType : 'text',
		type : 'GET',
		processData : false,
		contentType : "application/json",
		success : function(response, status, xhr) {
			if (status == "success") {
				// document.location.reload();
				createOptionElem(response);

			}
		},
		error : function(request, status, error) {

		},
		complete : function(jqXHR, textStatus) {

		}
	});
}

function loadSelector() {
	$.ajax({
		url : "selector",
		dataType : 'text',
		type : 'GET',
		processData : false,
		contentType : "application/json",
		success : function(response, status, xhr) {
			if (status == "success") {
				// document.location.reload();
				createSelectorOptionElem(response);

			}
		},
		error : function(request, status, error) {

		},
		complete : function(jqXHR, textStatus) {

		}
	});
}

function addSelector(selector) {
	$.ajax({
		url : "selector/add",
		dataType : 'text',
		type : 'POST',
		data : selector,
		processData : false,
		contentType : false,
		success : function(response, status, xhr) {
			if (status == "success") {
				// document.location.reload();
				createSelectorOptionElem(response);

			}
		},
		error : function(request, status, error) {

		},
		complete : function(jqXHR, textStatus) {

		}
	});
}

function deleteSelector(selector) {
	$.ajax({
		url : "selector/delete",
		dataType : 'text',
		type : 'POST',
		data : selector,
		processData : false,
		contentType : false,
		success : function(response, status, xhr) {
			if (status == "success") {
				// document.location.reload();
				createSelectorOptionElem(response);

			}
		},
		error : function(request, status, error) {

		},
		complete : function(jqXHR, textStatus) {

		}
	});
}

function createOptionElem(list) {
	list = JSON.parse(list);
	var pathSelect = $(".linkPath");

	$.each(pathSelect, function(i, select) {
		var defaultOption = document.createElement('option');
		defaultOption.textContent = "------";
		defaultOption.setAttribute('value', "-");
		$(this).append(defaultOption);

		for (var i = 0; i < list.length; i++) {
			var path = list[i];

			var option = document.createElement('option');
			option.textContent = "root\\" + path;

			option.setAttribute('value', path);

			// voiceSelect.appendChild(option);

			$(this).append(option);

		}
	});
}

function createSelectorOptionElem(selectorString) {
	list = selectorString.split(" ");
	var select = $("#linkSelector");
	
	select.empty();

	for (var i = 0; i < list.length; i++) {
		var path = list[i];

		var option = document.createElement('option');
		option.textContent = path;

		option.setAttribute('value', path);

		select.append(option);

	}

}
