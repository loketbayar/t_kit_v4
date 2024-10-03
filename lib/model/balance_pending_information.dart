class BalanceInformationModel {
  String? orderNo;
  String? balance;
  String? merchantName;
  String? timestamp;
  String? tid;
  String? mid;
  String? merchantAdress;
  String? bankName;
  String? cardNumber;
  String? accountNumber;
  String? noReff;

  BalanceInformationModel({
    this.orderNo,
    this.balance,
    this.merchantName,
    this.timestamp,
    this.tid,
    this.mid,
    this.merchantAdress,
    this.bankName,
    this.cardNumber,
    this.accountNumber,
    this.noReff,
  });

  BalanceInformationModel copyWith({
    String? orderNo,
    String? balance,
    String? merchantName,
    String? timestamp,
    String? tid,
    String? mid,
    String? merchantAdress,
    String? bankName,
    String? cardNumber,
    String? accountNumber,
    String? noReff,
  }) =>
      BalanceInformationModel(
        orderNo: orderNo ?? this.orderNo,
        balance: balance ?? this.balance,
        merchantName: merchantName ?? this.merchantName,
        timestamp: timestamp ?? this.timestamp,
        tid: tid ?? this.tid,
        mid: mid ?? this.mid,
        merchantAdress: merchantAdress ?? this.merchantAdress,
        bankName: bankName ?? this.bankName,
        cardNumber: cardNumber ?? this.cardNumber,
        accountNumber: accountNumber ?? this.accountNumber,
        noReff: noReff ?? this.noReff,
      );

  factory BalanceInformationModel.fromJson(Map<String, dynamic> json) => BalanceInformationModel(
        orderNo: json["orderNo"],
        balance: json["balance"],
        merchantName: json["merchantName"],
        timestamp: json["timestamp"],
        tid: json["tid"],
        mid: json["mid"],
        merchantAdress: json["merchantAdress"],
        bankName: json["bankName"],
        cardNumber: json["cardNumber"],
        accountNumber: json["accountNumber"],
        noReff: json["noReff"],
      );

  Map<String, dynamic> toJson() => {
        "orderNo": orderNo,
        "balance": balance,
        "merchantName": merchantName,
        "timestamp": timestamp,
        "tid": tid,
        "mid": mid,
        "merchantAdress": merchantAdress,
        "bankName": bankName,
        "cardNumber": cardNumber,
        "accountNumber": accountNumber,
        "noReff": noReff,
      };
}
