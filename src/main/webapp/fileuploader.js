var fileuploader = angular.module('fileuploader', []);

fileuploader.controller('mainController', ['$scope', '$filter','$http','$timeout', function($scope, $filter, $http, $timeout) {

	$scope.errorMessage = '';
	$scope.successUp = false;
    $scope.owner = '';
    $scope.filesInfo = [];

    $scope.lowercaseowner = function() {
      return $filter('lowercase')($scope.owner);
    };
    
    $scope.listFiles = function() {
    	$scope.successUp = false;
    	$http.get('/fileuploader/rest/files/list')
        .success(function(result){
        	$scope.filesInfo = result;
        })
        .error(function(data,status){
        	console.log(data);
        });
    };
    
    $scope.upload = function() {
      var file = document.getElementById('file').files[0];
      var fileName = file.name;	
      var chunkSize = 1024 * 1024; //1MB
      var numberChunks = Math.ceil(file.size/chunkSize);
      
      $scope.aux = 0;
      $scope.start = 0;
      $scope.stop = chunkSize;

      var blobs = [];

      while($scope.aux < numberChunks){
        var blob = file.slice($scope.start, $scope.stop + 1);
        blobs[$scope.aux] = blob;
        $scope.start = $scope.stop;
        $scope.stop += chunkSize;
        $scope.aux++;
      }
      var numberOfChunks = blobs.length;
      sendChunks(blobs,fileName,0,numberOfChunks);
      $scope.successUp = true;
    }

    var sendChunks = function(blobs, fileName, chunk, numberOfChunks){
    	if(chunk === numberOfChunks){
    		return;
    	}
        var formData = new FormData();
        formData.append('chunkNumber', chunk);
        formData.append('chunksExpected', blobs.length);
        formData.append('owner',$scope.owner);
        formData.append('name',fileName);
        formData.append('file',blobs[chunk]);
        $http.post('/fileuploader/rest/files/upload', formData, {headers: {'Content-Type': undefined }})
        	.then(function(response){
        		$scope.errorMessage = '';
        		sendChunks(blobs, fileName, chunk+1, numberOfChunks);
        }, function(response){
        	$scope.successUp = false;
        	$scope.errorMessage = response.data.message;
        });
    };
    
    $scope.download = function(test) {
    	console.log(test);
    	$http.get('/fileuploader/rest/files/download')
        .success(function(result){
          $scope.filesInfo = result;
        })
        .error(function(data,status){
          console.log(data);
        });
    }
    
    $scope.eraseDataBase = function() {
    	console.log();
    	$http.get('/fileuploader/rest/files/erase')
        .success(function(result){
        	$scope.filesInfo = [];
        })
        .error(function(data,status){
          console.log(data);
        });
    }
    
    $http.get('/fileuploader/rest/TestService/tests')
      .success(function(result){
        $scope.tests = result;
      })
      .error(function(data,status){
        console.log(data);
      });
}]);
