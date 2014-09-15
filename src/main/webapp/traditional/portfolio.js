
function ApplicationModel(stompClient) {
  var self = this;

  self.username = ko.observable();
  self.portfolio = ko.observable(new PortfolioModel());
  self.trade = ko.observable(new TradeModel(stompClient));
  self.notifications = ko.observableArray();
  self.feed = ko.observable(new FeedModel());

  self.connect = function() {
      stompClient.connect({}, function(frame) {

      console.log('Connected ' + frame);
      self.username(frame.headers['user-name']);

      stompClient.subscribe("/app/positions", function(message) {
        self.portfolio().loadPositions(JSON.parse(message.body));
      });
      stompClient.subscribe("/topic/price.stock.*", function(message) {
        self.portfolio().processQuote(JSON.parse(message.body));
      });
      stompClient.subscribe("/topic/price.currency.*", function(message) {
//    	  alert(message.body);
    	  self.feed().processFeed(JSON.parse(message.body));
    	  self.portfolio().updateCurrentPrice(JSON.parse(message.body));
      });
      stompClient.subscribe("/user/queue/position-updates", function(message) {
    	 var pos = JSON.parse(message.body);
        self.pushNotification("Positions update " + pos.length);
//        self.portfolio().clearPositions();
//        self.portfolio().loadPositions(JSON.parse(message.body));
        self.portfolio().updatePositions(JSON.parse(message.body));
      });
      stompClient.subscribe("/user/queue/errors", function(message) {
        self.pushNotification("Error " + message.body);
      });
    }, function(error) {
      console.log("STOMP protocol error " + error);
    });
  }

  self.pushNotification = function(text) {
    self.notifications.push({notification: text});
    if (self.notifications().length > 5) {
      self.notifications.shift();
    }
  }

  self.logout = function() {
    stompClient.disconnect();
    window.location.href = "../logout.html";
  }
}

function PortfolioModel() {
  var self = this;

  self.rows = ko.observableArray();

  self.totalShares = ko.computed(function() {
//    var result = 0;
//    for ( var i = 0; i < self.rows().length; i++) {
//      result += self.rows()[i].shares();
//    }
//    return result;
	  return self.rows().length;
  });

  self.totalValue = ko.pureComputed(function() {
    var result = 0;
    for ( var i = 0; i < self.rows().length; i++) {
      //result += self.rows()[i].value();
      result += self.rows()[i].value();
    }
    return result.toFixed(2);
  }).extend({notify : 'always'});

  var rowLookup = {};

  self.loadPositions = function(positions) {
    for ( var i = 0; i < positions.length; i++) {
      var row = new PortfolioRow(positions[i]);
      self.rows.push(row);
      //rowLookup[row.ticker] = row;
      for(var x=0; x<100; x++){
    	  if(rowLookup.hasOwnProperty(x)){
    	  }  else{
    		  rowLookup[x] = row; 
    		  break;
    	  }  	  
      }
    }
  };
  
  self.clearPositions = function() {
//	  alert("self.rows().length "+self.rows().length);
	  //self.rows([]);
//	  console.log("clearing positions... ");
	  for(var x=0; x<100; x++){
    	  if(rowLookup.hasOwnProperty(x)){
//    		  alert("clearing rowLookup property "+x);
    		  delete rowLookup[x];
    	  }  else{
    		  break;
    	  }  	  
      }
	  //console.log("self.rows.length "+self.rows().length);
	  //self.rows([]);
	  while(self.rows().length > 0){
		  self.rows.pop();		  
	  }
	  //console.log("positions cleared... ");
  };

  self.processQuote = function(quote) {
    if (rowLookup.hasOwnProperty(quote.ticker)) {
      rowLookup[quote.ticker].updatePrice(quote.price);
    }
  };

  self.updatePositions = function(positions) {
	  for ( var i = 0; i < positions.length; i++) {
		  var exists = false;
		  for (var x = 0; x < self.rows().length; x++) {
			  alert('self.rows[x].order '+self.rows[x].order);
			  if (self.rows[x].order == positions[i].order){
				  exists = true;
				  break;				  
			  }			  
		  }
		  if (!exists) {
			  var row = new PortfolioRow(positions[i]);
			  self.rows.push(row);
			  for(var x=0; x<100; x++){
		    	  if(rowLookup.hasOwnProperty(x)){
		    	  }  else{
		    		  rowLookup[x] = row; 
		    		  break;
		    	  }  	  
		      }			  			  
		  }	      
	    }
  };
  
  self.updateCurrentPrice = function(quote) {
//	  if (rowLookup.hasOwnProperty(quote.symbol)){
//		  rowLookup[quote.symbol].updateCurrentPrice(quote);	
//		  self.totalValue;
//	  }
	  for(var x=0; x<100; x++){
    	  if(rowLookup.hasOwnProperty(x)){
    		  if(rowLookup[x].symbol == quote.symbol){
    			  rowLookup[x].updateCurrentPrice(quote);    			  
    		  }
    	  }else{
    		  break;
    	  }  	  
      }
  };
};



function PortfolioRow(data) {
  var self = this;
  self.todo = ko.observable();
  self.order = data.order;
  self.type = ko.observable(data.type);
  self.symbol = data.symbol;
  self.company = data.company;
  self.ticker = data.ticker;
  self.size = data.size;
  self.price = ko.observable(data.price);
  self.currentPrice = ko.observable();
  self.tradeDate = data.tradeDate;
  self.formattedPrice = ko.computed(function() { return "$" + self.price().toFixed(2); });
  self.change = ko.observable(0);
  self.arrow = ko.observable();
  self.shares = ko.observable(data.shares);
  self.value = ko.observable();//ko.computed(function() { return (self.price() * self.shares()); });
  self.formattedValue =  ko.observable();//ko.computed(function() { return "$" + (100000*self.value()).toFixed(2); });

  self.updatePrice = function(newPrice) {
	  var delta = 0;
	  if(self.type() == 'Buy' || self.type() == 'buy'){
		  delta = (newPrice - self.price());		  
	  }else{
		  delta = (self.price() - newPrice);		  		  
	  }
    self.arrow((delta < 0) ? '<i class="icon-arrow-down"></i>' : '<i class="icon-arrow-up"></i>');
    self.change((delta).toFixed(5));
    //self.price(newPrice);
    self.value(delta * 100000);
    self.formattedValue((delta * 100000).toFixed(2));
  };
  
  self.updateCurrentPrice = function(quote) {
	  if(self.type() == 'Buy' || self.type() == 'buy'){
		  self.currentPrice(quote.bid);
		  self.updatePrice(quote.bid);
	  }else{
		  self.currentPrice(quote.ask);
		  self.updatePrice(quote.ask);
	  }
  };
};

function TradeModel(stompClient) {
  var self = this;

  self.action = ko.observable();
  self.sharesToTrade = ko.observable(0);
  self.currentRow = ko.observable({});
  self.error = ko.observable('');
  self.suppressValidation = ko.observable(false);

  self.showBuy  = function(row) { self.showModal('Buy', row) }
  self.showSell = function(row) { self.showModal('Sell', row) }
  self.showAction = function(row) { self.showModal('Action', row) }

  self.showModal = function(action, row) {
    self.action(action);
    self.sharesToTrade(0);
    self.currentRow(row);
    self.error('');
    self.suppressValidation(false);
    $('#trade-dialog').modal();
  }

  $('#trade-dialog').on('shown', function () {
    var input = $('#trade-dialog input');
    input.focus();
    input.select();
  })
  
  var validateShares = function() {
      if (isNaN(self.sharesToTrade()) || (self.sharesToTrade() < 0.1)) {
        self.error('Invalid number');
        return false;
      }
      if ((self.action() === 'Sell') && (self.sharesToTrade() > self.currentRow().shares())) {
        self.error('Not enough shares');
        return false;
      }
      return true;
  }
  
  self.clickAction = function(doAction, data, event){
	  alert(doAction);
	  self.action = doAction;
	  return true;
  }

  self.executeTrade = function(doAction, data, event) {
	//alert('from executeTrade '+ doAction);
    if (!self.suppressValidation() && !validateShares()) {
      return;
    }
    var trade = {
    	"order" : self.currentRow().order,
        "action" : doAction,
        "symbol" : self.currentRow().symbol,
        "size" : self.sharesToTrade()
      };
    console.log(trade);
    stompClient.send("/app/trade", {}, JSON.stringify(trade));
    $('#trade-dialog').modal('hide');
  }
}

function FeedModel() {
	  var self = this;
	  self.rows = ko.observableArray();

	  var feedRowLookup = {};
	  self.processFeed = function(fxquote) {
	    if (feedRowLookup.hasOwnProperty(fxquote.symbol)) {
	    	feedRowLookup[fxquote.symbol].updateBid(fxquote.bid);
	    	feedRowLookup[fxquote.symbol].updateAsk(fxquote.ask);
	    }else{
	    	var row = new FeedRow(fxquote);
	    	self.rows.push(row);
	    	feedRowLookup[fxquote.symbol] = row;
	    }
	  };
};

function FeedRow(data) {
	  var self = this;

	  self.symbol = data.symbol;
	  self.bid = ko.observable(data.bid);
	  self.ask = ko.observable(data.ask);
	  self.changeBid = ko.observable(0);
	  self.changeAsk = ko.observable(0);

	  self.updateBid = function(newPrice) {
	    var delta = (newPrice - self.bid());//.toFixed(2);
	    //self.changeBid((delta / self.bid() * 100).toFixed(2));
	    self.changeBid(delta);
	    self.bid(newPrice);
	  };
	  self.updateAsk = function(newPrice) {
		  var delta = (newPrice - self.ask());//.toFixed(2);
		  //self.changeAsk((delta / self.ask() * 100).toFixed(2));
		  self.changeAsk(delta);
		  self.ask(newPrice);
	  };
};
