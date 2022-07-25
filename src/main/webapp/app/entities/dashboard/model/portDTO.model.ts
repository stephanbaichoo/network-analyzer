export interface IPortDTO {

  id?: Number;

  port?: Number;

  isTCP?: String;

  isUDP?: String;

  isSCTP?: String;

  description?: String;

  name?: String

}

export class PortDTO implements IPortDTO {
  constructor(
    id: Number,
    port: Number,
    isTCP: String,
    isUDP: String,
    isSCTP: String,
    description: String,
    name: String
  ) {
  }
}
