sealed class TopwiseDataType {
  static const String magneticStripe = "magneticStripe";
  static const String icCard = "icCard";
  static const String nfcContactless = "nfcContactless";
  static const String qrCodeScanner = "qrCodeScanner";
  static const String printer = "printer";
  static const String shellCMD = "shellCMD";
}

class ShellCMD extends TopwiseDataType {
  final String? serialNumber;
  ShellCMD({
    this.serialNumber,
  });
}
