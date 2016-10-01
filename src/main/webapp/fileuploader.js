var fileuploader = angular.module('fileuploader', []);

fileuploader.controller('mainController', ['$scope', '$filter','$http','$timeout', function($scope, $filter, $http, $timeout) {

    $scope.owner = '';
    $scope.numChar = 5;

    $scope.lowercaseowner = function() {
      return $filter('lowercase')($scope.owner);
    };

    $scope.upload = function(){
      var file = document.getElementById('file').files[0];
      var fileName = file.name;	
      var chunkSize = 1024 * 1024; //1MB
      var numberChunks = Math.ceil(file.size/chunkSize);
      
      $scope.aux = 0;
      $scope.start = 0;
      $scope.stop = chunkSize;
      $scope.canRead = true;

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
    }

    var sendChunks = function(blobs, fileName, chunk, numberOfChunks){
    	if(chunk === numberOfChunks){
    		return;
    	}
        var formData = new FormData();
        formData.append('chunkNumber', chunk);
        formData.append('chunksExpected', blobs.length);
        formData.append('owner','felipe');
        formData.append('name',fileName);
        formData.append('file',blobs[chunk]);
        $http.post('/fileuploader/rest/files/upload', formData, {headers: {'Content-Type': undefined }})
        .success(function(result){
        	sendChunks(blobs, fileName, chunk+1, numberOfChunks);
        })
        .error(function(result,status){
        });
    };

    $http.get('/fileuploader/rest/TestService/tests')
      .success(function(result){
        $scope.tests = result;
      })
      .error(function(data,status){
        console.log(data);
      });
}]);
