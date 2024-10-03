import 'dart:convert';

class TopwiseDataChannel {
  const TopwiseDataChannel({
    required this.type,
    required this.data,
  });

  final String type;
  final String data;

  Map<String, dynamic> toMap() {
    return <String, dynamic>{
      'type': type,
      'data': data,
    };
  }

  factory TopwiseDataChannel.fromMap(Map<String, dynamic> map) {
    return TopwiseDataChannel(
      type: map['type'] as String,
      data: map['data'] as String,
    );
  }

  String toJson() => json.encode(toMap());

  factory TopwiseDataChannel.fromJson(String source) => TopwiseDataChannel.fromMap(json.decode(source) as Map<String, dynamic>);

  @override
  String toString() => 'TopwiseDataChannel(type: $type, data: $data)';

  @override
  bool operator ==(covariant TopwiseDataChannel other) {
    if (identical(this, other)) return true;

    return other.type == type && other.data == data;
  }

  @override
  int get hashCode => type.hashCode ^ data.hashCode;
}
