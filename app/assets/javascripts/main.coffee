app.controller "MainController", [
  "$scope"
  "$http"
  ($scope, $http) ->
    $scope.todaysPlaces = []

    onError = (data, status, headers, config) ->
      console.error(status, headers, config)

    $scope.loadList = () ->
      $http
        method: 'GET'
        url: '/todays-places'
      .success (data, status, headers, config) ->
        $scope.todaysPlaces = data
      .error onError

    $scope.recreateList = () ->
      $http
        method: 'POST'
        url: '/regenerate-places'
      .success (data, status, headers, config) ->
        $scope.todaysPlaces = data
      .error onError

    $scope.loadList()
]